package com.diy.app;

import com.diy.framework.web.JspView;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    LectureRepository lectureRepository = new LectureRepository();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        lectureRepository.save(new Lecture(1L, "이것이 자바다"));
        lectureRepository.save(new Lecture(2L, "스프링 만들기"));
    }

    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final Collection<Lecture> lectures = lectureRepository.values();
        req.setAttribute("lectures", lectures);

        final JspView jspView = new JspView("lecture-list.jsp");
        jspView.render(req, resp);
    }
}
