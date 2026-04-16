package com.diy.framework.web.server.handler;

import com.diy.framework.web.server.bean.BeanFactory;
import com.diy.framework.web.server.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 옛날 방식. URI 첫 segment == 빈 이름 규칙으로 Controller 인터페이스 구현체를 찾는다.
 */
public class BeanNameHandlerMapping implements HandlerMapping {

    private final Map<String, Controller> controllerMap = new HashMap<>();

    public BeanNameHandlerMapping(BeanFactory beanFactory) {
        controllerMap.putAll(beanFactory.getBeansOfType(Controller.class));
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] parts = uri.split("/");
        if (parts.length < 2) {
            return null;
        }

        String beanName = parts[1];
        Controller controller = controllerMap.get(beanName);
        if (controller == null) {
            return null;
        }

        // 기존 컨트롤러 내부에서 pathInfo를 참조하니까 여기서 세팅해준다.
        String pathInfo = uri.substring(("/" + beanName).length());
        if (pathInfo.isEmpty()) {
            pathInfo = "/";
        }
        request.setAttribute("pathInfo", pathInfo);

        return controller;
    }
}
