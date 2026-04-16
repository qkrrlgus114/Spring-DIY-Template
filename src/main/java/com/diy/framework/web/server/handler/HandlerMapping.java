package com.diy.framework.web.server.handler;

import javax.servlet.http.HttpServletRequest;

/**
 * 요청에 맞는 핸들러를 찾아주는 역할.
 * <p>
 * 핸들러가 Controller 인터페이스 구현체일 수도 있고, MethodHandler 일 수도 있다.
 * 타입은 모르니까 Object로 돌려주고, 누가 실행할지는 HandlerAdapter 쪽에서 결정한다.
 */
public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);
}
