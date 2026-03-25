package com.diy.framework.web.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * 컨트롤러 인터페이스
 * */
@FunctionalInterface
public interface Controller {
    void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception;
}
