package com.diy.framework.web.server.handler;

import com.diy.framework.web.server.controller.Controller;
import com.diy.framework.web.server.model.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 인터페이스 기반 Controller 를 실행한다.
public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        return ((Controller) handler).handleRequest(req, res);
    }
}
