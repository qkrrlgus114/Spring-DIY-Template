package com.diy.framework.web.mvc.controller;

import com.diy.framework.context.HandlerMethod;
import com.diy.framework.context.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {

    private final Map<String, HandlerMethod> mapper = new HashMap<>();

    public HandlerMapping() {
    }

    public void initialize(Map<Class<?>, Object> beans) {
        for (Object bean : beans.values()) {
            Class<?> beanClass = bean.getClass();

            // 1. 클래스 레벨의 @RequestMapping 스캔
            String baseUrl = "";
            if (beanClass.isAnnotationPresent(RequestMapping.class)) {
                baseUrl = beanClass.getAnnotation(RequestMapping.class).value();
            }

            // 2. 메서드 레벨의 @RequestMapping 스캔
            Method[] methods = beanClass.getDeclaredMethods();
            for (Method method: methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    String url = baseUrl + mapping.value();

                    mapper.put(url, new HandlerMethod(bean, method));
                    System.out.println("[HandlerMapping] " + url + " -> " + beanClass.getSimpleName() + "." + method.getName());
                }
            }
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return mapper.get(uri);
    }
}
