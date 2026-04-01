package com.diy.framework.web.server.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 빈 관련된 모든 것을 담아두겠다. 하하
 * <p>
 * - 빈 스캐너도 여기 두고..
 * - 또 뭐 있으려나
 */
public class BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);
    private Set<Class<?>> classes;

    private final Map<Class<?>, Object> store = new HashMap<>();

    public void start() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BeanScanner beanScanner = new BeanScanner("com.diy");

        classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        for (Class<?> clazz : classes) {
            createBean(clazz);
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

        for (Class<?> candidate : classes) {
            if (parameterType.isAssignableFrom(candidate)) {
                return createBean(candidate);
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
}
