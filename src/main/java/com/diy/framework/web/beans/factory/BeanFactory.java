package com.diy.framework.web.beans.factory;

import com.diy.framework.context.Autowired;
import com.diy.framework.context.Component;

import java.lang.reflect.Constructor;
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
            createBean(beanClass);
        }
    }

    private Object createBean(Class<?> beanClass) throws Exception {
        Constructor<?> constructor = null;
        for (Constructor<?> c : beanClass.getDeclaredConstructors()) {
            if (c.isAnnotationPresent(Autowired.class)) {
                constructor = c;
                break;
            }
        }

        // 없으면 기본 생성자
        if (constructor == null) {
            constructor = beanClass.getDeclaredConstructor();
        }

        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = findOrCreateBean(paramTypes[i]);
        }

        Object bean = constructor.newInstance(args);
        beans.put(beanClass, bean);
        return bean;
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

}
