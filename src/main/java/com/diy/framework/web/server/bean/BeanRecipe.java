package com.diy.framework.web.server.bean;

/**
 * Component든 Bean이든 결국 목적은 빈을 등록시키기 위한 표시일 뿐
 * <p>
 * 이 인터페이스를 이용해 빈 등록을 추상화시키려고 한다.
 */
public interface BeanRecipe {
    String getName();

    Class<?> getType();

    Object create();
}
