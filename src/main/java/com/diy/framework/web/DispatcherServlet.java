package com.diy.framework.web;

import com.diy.app.Lecture;
import com.diy.app.LectureController;
import com.diy.app.LectureRepository;
import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.ViewResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Front Controller 역할
 * - 모든 HTTP 요청을 가장 먼저 받아 공통 작업을 처리한 후 적절한 컨트롤러로 요청을 위임.
 */
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> handlerMap = new HashMap<>();

    @Override
    public void init() {
        JspViewResolver jspViewResolver = new JspViewResolver();
        LectureRepository lectureRepository = new LectureRepository();
        lectureRepository.save(new Lecture(1L, "이것이 자바다"));
        lectureRepository.save(new Lecture(2L, "스프링 만들기"));

        Controller lectureController = new LectureController(lectureRepository, jspViewResolver);
        handlerMap.put("GET /lectures", lectureController);
        handlerMap.put("POST /lectures", lectureController);
        handlerMap.put("PUT /lectures", lectureController);
        handlerMap.put("DELETE /lectures", lectureController);
        handlerMap.put("GET /lectures/edit", lectureController);
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        // 한글 인코딩
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String method = req.getMethod();
        if ("POST".equals(method)) {
            String override = req.getParameter("_method");
            if (override != null) method = override.toUpperCase();
        }
        String key = method + " " + req.getRequestURI();
        Controller controller = handlerMap.get(key);

        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            controller.handleRequest(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private Map<String, ?> parseParams(final HttpServletRequest req) throws IOException {
        if ("application/json".equals(req.getHeader("Content-Type"))) {
            final byte[] bodyBytes = req.getInputStream().readAllBytes();
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {
            });
        } else {
            return req.getParameterMap();
        }
    }
}
