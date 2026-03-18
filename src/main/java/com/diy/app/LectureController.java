package com.diy.app;

import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.view.JspView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LectureController implements Controller {
    private final LectureRepository lectureRepository;

    public LectureController(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        switch (request.getMethod()) {
            case "GET" -> handleGet(request, response);
            case "POST" -> handlePost(request, response);
            case "PUT" -> handlePut(request, response);
            case "DELETE" -> handleDelete(request, response);

        }
    }

    private void handleGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("lectures", lectureRepository.values());
        new JspView("lecture-list.jsp").render(request, response);
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response) {

    }

    private void handlePut(HttpServletRequest request, HttpServletResponse response) {

    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) {

    }
}
