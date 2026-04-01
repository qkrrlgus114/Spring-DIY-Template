package com.diy.app;

import com.diy.framework.context.Autowired;
import com.diy.framework.context.Component;
import com.diy.framework.context.RequestMapping;
import com.diy.framework.web.mvc.Model;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequestMapping("/lectures")
public class LectureController implements Controller {
    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();
        String override = request.getParameter("_method");
        if (override != null) method = override.toUpperCase();

        return switch (method) {
            case "GET" -> handleGet(request, response);
            case "POST" -> handlePost(request, response);
            case "PUT" -> handlePut(request, response);
            case "DELETE" -> handleDelete(request, response);
            default -> throw new IllegalStateException("Unexpected value: " + method);
        };
    }

    private ModelAndView handleGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURI().contains("/edit")) {
            Long id = Long.valueOf(request.getParameter("id"));
            Model model = new Model().addAttribute("lecture", lectureService.getLecture(id));
            return new ModelAndView("lecture-edit", model);
        } else {
            Model model = new Model().addAttribute("lectures", lectureService.getLectures());
            return new ModelAndView("lecture-list", model);
        }
    }

    private ModelAndView handlePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String title = request.getParameter("title");
        lectureService.saveLecture(title);
        return new ModelAndView("redirect:/lectures", new Model());
    }

    private ModelAndView handlePut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.valueOf(request.getParameter("id"));
        String title = request.getParameter("title");
        Lecture lecture = lectureService.getLecture(id);
        lecture.setTitle(title);
        lectureService.updateLecture(lecture);
        return new ModelAndView("redirect:/lectures", new Model());
    }

    private ModelAndView handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.valueOf(request.getParameter("id"));
        lectureService.deleteLecture(id);
        return new ModelAndView("redirect:/lectures", new Model());
    }
}
