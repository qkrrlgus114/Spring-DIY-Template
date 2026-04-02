package com.diy.framework.web.server.bean;

public class ComponentBeanRecipe implements BeanRecipe {

    private final Class<?> clazz;

    public ComponentBeanRecipe(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<?> getType() {
        return clazz;
    }

    @Override
    public Object create() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("빈 생성 실패: " + clazz.getName(), e);
        }
    }
}
