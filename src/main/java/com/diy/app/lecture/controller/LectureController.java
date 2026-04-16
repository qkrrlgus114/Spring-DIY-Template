package com.diy.app.lecture.controller;

import com.diy.app.lecture.model.Lecture;
import com.diy.app.lecture.service.LectureService;
import com.diy.framework.web.server.bean.Autowired;
import com.diy.framework.web.server.bean.Controller;
import com.diy.framework.web.server.bean.RequestMapping;
import com.diy.framework.web.server.bean.RequestMethod;
import com.diy.framework.web.server.model.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(value = "/register", methods = RequestMethod.GET)
    public ModelAndView showRegisterPage(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView("lecture-registration.jsp");
    }

    @RequestMapping(value = "", methods = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest req, HttpServletResponse res) {
        List<Lecture> lectures = lectureService.findAllLecture();
        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);
        return new ModelAndView("lecture-list.jsp", model);
    }

    @RequestMapping(value = "", methods = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureService.register(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(value = "", methods = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureService.updateLecture(lecture);
        res.setStatus(HttpServletResponse.SC_OK);
        return null;
    }

    @RequestMapping(value = "/*", methods = RequestMethod.DELETE)
    public ModelAndView delete(HttpServletRequest req, HttpServletResponse res) {
        String pathInfo = (String) req.getAttribute("pathInfo");
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
