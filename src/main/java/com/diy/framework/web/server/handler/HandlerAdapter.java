package com.diy.framework.web.server.handler;

import com.diy.framework.web.server.model.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 찾아낸 핸들러를 실제로 실행시키는 역할.
 * <p>
 * 핸들러 타입마다 실행 방식이 달라서 (interface Controller vs MethodHandler),
 * 각 방식마다 Adapter를 하나씩 만들어둔다.
 */
public interface HandlerAdapter {
    boolean supports(Object handler);

    ModelAndView handle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception;
}
