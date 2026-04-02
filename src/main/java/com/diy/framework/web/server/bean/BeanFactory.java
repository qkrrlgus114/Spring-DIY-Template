package com.diy.framework.web.server.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 빈 관련된 모든 것을 담아두겠다. 하하
 * <p>
 * - 빈 스캐너도 여기 두고..
 * - 또 뭐 있으려나
 */
public class BeanFactory {

    private List<BeanRecipe> recipes;

    private final Map<Class<?>, Object> store = new HashMap<>();

    public void start() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BeanScanner beanScanner = new BeanScanner("com.diy");
        // 빈 등록 대상 전체 수집
        recipes = collectAllBean(beanScanner);

        // 빈 등록
        registerBean();
    }

    private void registerBean() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (BeanRecipe recipe : recipes) {
            if (!store.containsKey(recipe.getType())) {
                // @Autowired(Component) 있는 애들은 createBean으로 다 조회해서 등록
                if (recipe instanceof ComponentBeanRecipe) {
                    Object instance = createBean(recipe.getType());
                    store.put(recipe.getType(), instance);
                }
                // @Bean 애들은 create해서 바로 등록
                else {
                    Object instance = recipe.create();
                    store.put(recipe.getType(), instance);
                }
            }
        }
    }

    // 오토와이어드 붙은 생성자 찾기
    private Constructor<?> findAutowiredAnnotationConstructor(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                return constructor;
            }
        }
        return clazz.getDeclaredConstructor();
    }

    // 빈 생성 메서드
    private Object createBean(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (store.containsKey(clazz)) {
            return store.get(clazz);
        }

        Constructor<?> autowiredAnnotationConstructor = findAutowiredAnnotationConstructor(clazz);
        Class<?>[] parameterTypes = autowiredAnnotationConstructor.getParameterTypes();

        // 기본 생성자가 있으면 바로 리턴
        if (parameterTypes.length == 0) {
            return saveBean(autowiredAnnotationConstructor, clazz, null);
        }

        Object[] params = resolveConstructorArguments(parameterTypes);

        return saveBean(autowiredAnnotationConstructor, clazz, params);
    }

    // 파라미터 타입의 빈 조회
    private Object[] resolveConstructorArguments(Class<?>[] parameterTypes) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            params[i] = resolveDependency(parameterTypes[i]);
        }

        return params;
    }

    private Object resolveDependency(Class<?> parameterType) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!parameterType.isInterface()) {
            return createBean(parameterType);
        }

        for (BeanRecipe recipe : recipes) {
            if (parameterType.isAssignableFrom(recipe.getType())) {
                return createBean(recipe.getType());
            }
        }
        throw new IllegalArgumentException("구현체를 찾을 수 없습니다: " + parameterType.getName());
    }

    private Object saveBean(Constructor<?> constructor, Class<?> clazz, Object[] params) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object instance;
        if (params == null) {
            instance = constructor.newInstance();
        } else {
            instance = constructor.newInstance(params);
        }
        store.put(clazz, instance);
        return instance;
    }

    private List<BeanRecipe> collectAllBean(BeanScanner beanScanner) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<BeanRecipe> beanRecipeList = new ArrayList<>();

        // Component 어노테이션 수집
        Set<Class<?>> componentClasses = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        for (Class<?> clazz : componentClasses) {
            beanRecipeList.add(new ComponentBeanRecipe(clazz));
        }

        // Configuration 어노테이션 수집
        Set<Class<?>> configClasses = beanScanner.scanClassesTypeAnnotatedWith(Configuration.class);
        for (Class<?> configClass : configClasses) {
            for (Method method : configClass.getDeclaredMethods()) {
                // Bean이 있는 메서드만 찾는다.
                if (method.isAnnotationPresent(Bean.class)) {
                    Object object = configClass.getDeclaredConstructor().newInstance();
                    beanRecipeList.add(new ConfigurationBeanRecipe(object, method));
                }
            }
        }

        return beanRecipeList;
    }
}
