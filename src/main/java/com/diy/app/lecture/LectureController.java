package com.diy.app.lecture;

import com.diy.framework.web.server.controller.Controller;
import com.diy.framework.web.server.model.Model;
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
    public String handleRequest(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
        String method = req.getMethod();
        String pathInfo = req.getAttribute("pathInfo").toString();

        switch (method) {
            case "GET":
                return doGet(req, res, pathInfo, model);
            case "POST":
                return doPost(req, res);
            case "PUT":
                return doPut(req, res);
            case "DELETE":
                return doDelete(req, res, pathInfo);
            default:
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return null;
        }
    }

    private String doGet(HttpServletRequest req, HttpServletResponse res, String pathInfo, Model model) throws Exception {
        if ("/register".equals(pathInfo)) {
            return "lecture-registration.jsp";
        }

        List<Lecture> lectures = lectureService.findAllLecture();
        model.setData("lectures", lectures);
        return "lecture-list.jsp";
    }

    private String doPost(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureService.register(lecture);
        res.setStatus(HttpServletResponse.SC_OK);

        return null;
    }

    private String doPut(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureService.updateLecture(lecture);
        res.setStatus(HttpServletResponse.SC_OK);

        return null;
    }

    private String doDelete(HttpServletRequest req, HttpServletResponse res, String pathInfo) throws Exception {
        if (pathInfo == null || "/".equals(pathInfo)) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        long id = Long.parseLong(pathInfo.substring(1));
        lectureService.deleteLecture(id);
        res.setStatus(HttpServletResponse.SC_OK);

        return null;
    }
}
