package com.diy.framework.web.mvc.controller;

import com.diy.framework.context.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {

    private final Map<String, Controller> mapper = new HashMap<>();

    public HandlerMapping() {
    }

    public void initialize(Map<Class<?>, Object> beans) {
        for (Object bean : beans.values()) {
            Class<?> beanClass = bean.getClass();

            if (beanClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = beanClass.getAnnotation(RequestMapping.class);
                String url = requestMapping.value();

                if (bean instanceof Controller controller) {
                    mapper.put(url, controller);
                    System.out.println("[HandlerMapping] " + url + " -> " + beanClass.getSimpleName());
                }
            }
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();

        // 정확히 일치하는 url 우선
        if (mapper.containsKey(uri)) {
            return mapper.get(uri);
        }

        // prefix 일치하는 url 리턴
        return mapper.entrySet().stream()
                .filter(entry -> uri.startsWith(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
}
