package com.diy.framework.web.mvc.controller;

import com.diy.framework.context.HandlerMethod;
import com.diy.framework.context.RequestMapping;
import com.diy.framework.context.RequestMappingInfo;
import com.diy.framework.context.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {

    private final Map<RequestMappingInfo, HandlerMethod> mapper = new HashMap<>();

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
            for (Method method : beanClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    String url = baseUrl + mapping.value();

                    RequestMethod[] methods = mapping.methods();
                    for (RequestMethod requestMethod : methods) {
                        RequestMappingInfo requestInfo = new RequestMappingInfo(url, requestMethod);
                        mapper.put(requestInfo, new HandlerMethod(bean, method));
                        System.out.println("[HandlerMapping] " + requestInfo + " -> " + beanClass.getSimpleName() + "." + method.getName());
                    }
                }
            }
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        RequestMappingInfo requestInfo = new RequestMappingInfo(uri, method);
        return mapper.get(requestInfo);
    }
}
