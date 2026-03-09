package com.diy.app.lecture;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 강의 목록 조회 서블릿
 *
 */
@WebServlet("/lecture-list")
public class LectureListServlet extends HttpServlet {

    private LectureService lectureService = new LectureService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Lecture> lectures = lectureService.findAllLecture();

        req.setAttribute("lectures", lectures);
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }
}
