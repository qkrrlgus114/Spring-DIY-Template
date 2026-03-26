package com.diy.app.lecture;

import com.diy.framework.web.server.controller.Controller;
import com.diy.framework.web.server.model.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectureController implements Controller {

    private final LectureService lectureService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String method = req.getMethod();
        String pathInfo = req.getAttribute("pathInfo").toString();

        switch (method) {
            case "GET":
                return doGet(req, res, pathInfo);
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

    private ModelAndView doGet(HttpServletRequest req, HttpServletResponse res, String pathInfo) throws Exception {
        if ("/register".equals(pathInfo)) {
            return new ModelAndView("lecture-registration.jsp");
        }

        List<Lecture> lectures = lectureService.findAllLecture();
        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);
        return new ModelAndView("lecture-list.jsp", model);
    }

    private ModelAndView doPost(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureService.register(lecture);

        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doPut(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureService.updateLecture(lecture);

        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doDelete(HttpServletRequest req, HttpServletResponse res, String pathInfo) throws Exception {
        if (pathInfo == null || "/".equals(pathInfo)) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        long id = Long.parseLong(pathInfo.substring(1));
        lectureService.deleteLecture(id);

        return new ModelAndView("redirect:/lectures");
    }
}
