package com.diy.app.lecture;

import com.diy.framework.web.server.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class LectureController implements Controller {

    private final LectureService lectureService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String method = req.getMethod();
        String pathInfo = req.getAttribute("pathInfo").toString();

        switch (method) {
            case "GET":
                doGet(req, res, pathInfo);
                break;
            case "POST":
                doPost(req, res);
                break;
            case "PUT":
                doPut(req, res);
                break;
            case "DELETE":
                doDelete(req, res, pathInfo);
                break;
            default:
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private void doGet(HttpServletRequest req, HttpServletResponse res, String pathInfo) throws Exception {
        if ("/register".equals(pathInfo)) {
            req.getRequestDispatcher("/lecture-registration.jsp").forward(req, res);
            return;
        }

        List<Lecture> lectures = lectureService.findAllLecture();
        req.setAttribute("lectures", lectures);
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, res);
    }

    private void doPost(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureService.register(lecture);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    private void doPut(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureService.updateLecture(lecture);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    private void doDelete(HttpServletRequest req, HttpServletResponse res, String pathInfo) throws Exception {
        if (pathInfo == null || "/".equals(pathInfo)) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        long id = Long.parseLong(pathInfo.substring(1));
        lectureService.deleteLecture(id);
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
