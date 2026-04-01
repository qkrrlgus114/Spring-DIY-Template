package com.diy.framework.web.beans.factory;

import com.diy.framework.context.Autowired;
import com.diy.framework.context.Component;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    /**
     * 빈 스캔 -> 생성
     */
    public BeanFactory(String... basePackages) throws Exception {
        BeanScanner scanner = new BeanScanner(basePackages);
        Set<Class<?>> beanClasses = scanner.scanClassesTypeAnnotatedWith(Component.class);

        for (Class<?> beanClass : beanClasses) {
            if (!beans.containsKey(beanClass)) {
                createBean(beanClass);
            }
        }
    }

    private Object createBean(Class<?> beanClass) throws Exception {
        Constructor<?> constructor = getConstructor(beanClass);

        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = findOrCreateBean(paramTypes[i]);
        }

        Object bean = constructor.newInstance(args);
        beans.put(beanClass, bean);
        return bean;
    }

    private static Constructor<?> getConstructor(Class<?> beanClass) throws NoSuchMethodException {
        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();

        // 1. @Autowired 붙은 생성자 우선
        for (Constructor<?> c : constructors) {
            if (c.isAnnotationPresent(Autowired.class)) {
                return c;
            }
        }

        // 2. 단일 생성자 (자동 주입)
        if (constructors.length == 1) {
            return constructors[0];
        }

        // 3. 기본 생성자 반환
        try {
            return beanClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException(beanClass.getName() + " no constructor");
        }
    }

    private Object findOrCreateBean(Class<?> type) throws Exception {
        if (beans.containsKey(type)) {
            return beans.get(type);
        }
        return createBean(type);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        return (T) beans.get(type);
    }

    public Map<Class<?>, Object> getBeans() {
        return Collections.unmodifiableMap(this.beans);
    }
}
