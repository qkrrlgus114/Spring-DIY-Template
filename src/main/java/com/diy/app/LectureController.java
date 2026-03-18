package com.diy.app;

import com.diy.framework.web.mvc.Model;
import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LectureController implements Controller {
    private final LectureRepository lectureRepository;
    private final ViewResolver viewResolver;

    public LectureController(LectureRepository lectureRepository, ViewResolver viewResolver) {
        this.lectureRepository = lectureRepository;
        this.viewResolver = viewResolver;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();
        String override = request.getParameter("_method");
        if (override != null) method = override.toUpperCase();

        switch (method) {
            case "GET" -> handleGet(request, response);
            case "POST" -> handlePost(request, response);
            case "PUT" -> handlePut(request, response);
            case "DELETE" -> handleDelete(request, response);
        }
    }

    private void handleGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURI().contains("/edit")) {
            Long id = Long.valueOf(request.getParameter("id"));
            Model model = new Model().addAttribute("lectures", lectureRepository.findById(id));
            viewResolver.resolveViewName("lecture-edit").render(request, response, model);
        } else {
            Model model = new Model().addAttribute("lectures", lectureRepository.values());
            viewResolver.resolveViewName("lecture-list").render(request, response, model);
        }
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        lectureRepository.save(new Lecture(lectureRepository.nextId(), title));
        response.sendRedirect("/lectures");
    }

    private void handlePut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.valueOf(request.getParameter("id"));
        String title = request.getParameter("title");
        Lecture lecture = lectureRepository.findById(id);
        lecture.setTitle(title);
        lectureRepository.save(lecture);
        response.sendRedirect("/lectures");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.valueOf(request.getParameter("id"));
        lectureRepository.delete(id);
        response.sendRedirect("/lectures");
    }
}
