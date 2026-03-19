package com.diy.app.lecture;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 강의 서블릿
 *
 */
@WebServlet("/lectures/*")
public class LectureServlet extends HttpServlet {

    private final LectureService lectureService = new LectureService();

    /*
     * GET /lectures → 강의 목록 조회
     * GET /lectures/register → 강의 등록 화면
     * */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/register")) {
            req.getRequestDispatcher("/lecture-registration.jsp").forward(req, resp);
            return;
        }

        List<Lecture> lectures = lectureService.findAllLecture();
        req.setAttribute("lectures", lectures);
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    /*
     * POST /lectures → 강의 등록
     * */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);

        lectureService.register(lecture);

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /*
     * DELETE /lectures/{id} → 강의 삭제
     * */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        long id = Long.parseLong(pathInfo.substring(1));

        lectureService.deleteLecture(id);

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /*
     * PUT /lectures → 강의 수정
     * */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);

        lectureService.updateLecture(lecture);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
