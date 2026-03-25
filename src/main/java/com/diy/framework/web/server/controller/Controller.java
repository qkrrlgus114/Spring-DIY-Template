package com.diy.framework.web.server.controller;

import com.diy.framework.web.server.model.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * 컨트롤러 인터페이스
 * */
@FunctionalInterface
public interface Controller {
    String handleRequest(final HttpServletRequest request, final HttpServletResponse response, Model model) throws Exception;
}
