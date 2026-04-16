package com.diy.framework.web.server.handler;

import com.diy.framework.web.server.bean.BeanFactory;
import com.diy.framework.web.server.bean.Controller;
import com.diy.framework.web.server.bean.RequestMapping;
import com.diy.framework.web.server.bean.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 애너테이션 기반 컨트롤러 매핑.
 * <p>
 * @Controller 가 붙은 빈들을 훑어서 @RequestMapping 메서드를 하나씩 키로 등록한다.
 * 키 형식: "HTTP메서드:전체경로" (예: "GET:/lectures/register")
 */
public class AnnotationHandlerMapping implements HandlerMapping {

    private final Map<String, MethodHandler> handlerMap = new HashMap<>();

    public AnnotationHandlerMapping(BeanFactory beanFactory) {
        // 전체 빈 중에서 @Controller 가 붙은 것만 걸러낸다
        Map<String, Object> allBeans = beanFactory.getBeansOfType(Object.class);
        for (Object bean : allBeans.values()) {
            Class<?> clazz = bean.getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }

            String classPath = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                classPath = clazz.getAnnotation(RequestMapping.class).value();
            }

            for (Method method : clazz.getDeclaredMethods()) {
                RequestMapping rm = method.getAnnotation(RequestMapping.class);
                if (rm == null) {
                    continue;
                }

                String fullPath = classPath + rm.value();
                for (RequestMethod httpMethod : rm.methods()) {
                    String key = toKey(httpMethod.name(), fullPath);
                    handlerMap.put(key, new MethodHandler(bean, method));
                }
            }
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // 끝에 붙은 '/' 제거 (길이 1 일 때는 그대로 둔다)
        if (uri.length() > 1 && uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        String httpMethod = request.getMethod();

        // 1) 정확히 일치
        MethodHandler handler = handlerMap.get(toKey(httpMethod, uri));
        if (handler != null) {
            return handler;
        }

        // 2) 마지막 segment를 "/*" 와일드카드로 매칭 (예: /lectures/123 → /lectures/*)
        int lastSlash = uri.lastIndexOf('/');
        if (lastSlash > 0) {
            String parent = uri.substring(0, lastSlash);
            handler = handlerMap.get(toKey(httpMethod, parent + "/*"));
            if (handler != null) {
                // 잘라낸 꼬리를 pathInfo로 넘겨서 컨트롤러에서 쓸 수 있게 해준다
                request.setAttribute("pathInfo", uri.substring(lastSlash));
                return handler;
            }
        }

        return null;
    }

    private String toKey(String httpMethod, String path) {
        return httpMethod + ":" + path;
    }
}
