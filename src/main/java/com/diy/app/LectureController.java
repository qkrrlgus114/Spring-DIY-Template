package com.diy.app;

import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.view.JspView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureController implements Controller {
    private final LectureRepository lectureRepository;

    public LectureController(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("lectures", lectureRepository.values());
        new JspView("lecture-list.jsp").render(request, response);
    }
}
