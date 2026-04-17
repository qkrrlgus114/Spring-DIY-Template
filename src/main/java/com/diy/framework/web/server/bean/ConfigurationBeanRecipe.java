package com.diy.framework.web.server.bean;

import com.diy.framework.web.server.bean.annotation.Bean;

import java.lang.reflect.Method;

public class ConfigurationBeanRecipe implements BeanRecipe {

    private final Object configInstance;
    private final Method method;
    private final String name;

    public ConfigurationBeanRecipe(Object configInstance, Method method) {
        this.configInstance = configInstance;
        this.method = method;
        this.name = method.getAnnotation(Bean.class).value();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return method.getReturnType();
    }

    @Override
    public Object create() {
        try {
            return method.invoke(configInstance);
        } catch (Exception e) {
            throw new RuntimeException("@Bean 메서드 실행 실패: " + method.getName(), e);
        }
    }
}
