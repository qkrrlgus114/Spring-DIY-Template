package com.diy.framework.web.server.handler;

import com.diy.framework.web.server.model.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 애너테이션 기반 핸들러(MethodHandler)를 실행한다.
public class MethodHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof MethodHandler;
    }

    @Override
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        return ((MethodHandler) handler).invoke(req, res);
    }
}
