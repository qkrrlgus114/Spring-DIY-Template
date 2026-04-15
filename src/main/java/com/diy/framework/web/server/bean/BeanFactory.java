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

    private final Map<String, Object> store = new HashMap<>();

    public void start() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BeanScanner beanScanner = new BeanScanner("com.diy");
        // 빈 등록 대상 전체 수집
        recipes = collectAllBean(beanScanner);

        // 빈 등록
        registerBean();
    }

    public Object getBean(String name) {
        return store.get(name);
    }

    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<String, Object> e : store.entrySet()) {
            if (type.isAssignableFrom(e.getValue().getClass())) {
                result.put(e.getKey(), type.cast(e.getValue()));
            }
        }
        return result;
    }

    private void registerBean() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (BeanRecipe recipe : recipes) {
            if (!store.containsKey(recipe.getName())) {
                createBean(recipe);
            }
        }
    }

    // 레시피 기반으로 빈 생성
    private Object createBean(BeanRecipe recipe) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (store.containsKey(recipe.getName())) {
            return store.get(recipe.getName());
        }

        Object instance;
        if (recipe instanceof ComponentBeanRecipe) {
            Class<?> clazz = recipe.getType();
            Constructor<?> constructor = findAutowiredAnnotationConstructor(clazz);
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            if (parameterTypes.length == 0) {
                instance = constructor.newInstance();
            } else {
                Object[] params = resolveConstructorArguments(parameterTypes);
                instance = constructor.newInstance(params);
            }
        } else {
            // @Bean 메서드는 그대로 호출
            instance = recipe.create();
        }

        store.put(recipe.getName(), instance);
        return instance;
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

    // 파라미터 타입의 빈 조회
    private Object[] resolveConstructorArguments(Class<?>[] parameterTypes) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            params[i] = resolveDependency(parameterTypes[i]);
        }

        return params;
    }

    private Object resolveDependency(Class<?> parameterType) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // 이미 생성된 빈 중에서 먼저 찾고
        for (Object bean : store.values()) {
            if (parameterType.isAssignableFrom(bean.getClass())) {
                return bean;
            }
        }
        // 없으면 레시피에서 찾아 생성
        for (BeanRecipe recipe : recipes) {
            if (parameterType.isAssignableFrom(recipe.getType())) {
                return createBean(recipe);
            }
        }
        throw new IllegalArgumentException("구현체를 찾을 수 없습니다: " + parameterType.getName());
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
