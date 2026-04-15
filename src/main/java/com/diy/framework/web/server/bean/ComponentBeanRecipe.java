package com.diy.framework.web.server.bean;

public class ComponentBeanRecipe implements BeanRecipe {

    private final Class<?> clazz;
    private final String name;

    public ComponentBeanRecipe(Class<?> clazz) {
        this.clazz = clazz;
        String simple = clazz.getSimpleName();
        // 클래스의 첫글자를 소문자로 바꾼다.(스프링 빈 등록 컨벤션을 따름)
        this.name = Character.toLowerCase(simple.charAt(0)) + simple.substring(1);
    }

    @Override
    public String getName() {
        return name;
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
