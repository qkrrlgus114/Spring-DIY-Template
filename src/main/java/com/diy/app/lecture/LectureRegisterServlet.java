package com.diy.app.lecture;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 강의 목록 등록 서블릿
 *
 */
@WebServlet("/lecture-registration")
public class LectureRegisterServlet extends HttpServlet {

    private LectureService lectureService = new LectureService();

    /*
     * 강의 등록화면 제공
     * */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/lecture-registration.jsp").forward(req, resp);
    }

    /*
     * 강의 등록하기
     * */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureService.register(lecture);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
