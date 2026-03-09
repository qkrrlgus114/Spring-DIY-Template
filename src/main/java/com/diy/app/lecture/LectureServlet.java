package com.diy.app.lecture;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 강의 서블릿
 *
 */
@WebServlet("/lectures/*")
public class LectureServlet extends HttpServlet {

    private final LectureService lectureService = new LectureService();

    /*
     * 강의 삭제하기
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
     * 강의 수정하기
     * */

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);

        lectureService.updateLecture(lecture);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
