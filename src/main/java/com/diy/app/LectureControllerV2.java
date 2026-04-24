package com.diy.app;

import com.diy.framework.context.Autowired;
import com.diy.framework.context.Controller;
import com.diy.framework.context.RequestMapping;
import com.diy.framework.context.RequestMethod;
import com.diy.framework.web.mvc.Model;
import com.diy.framework.web.mvc.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/v2/lectures")
public class LectureControllerV2 {
    private final LectureService lectureService;

    @Autowired
    public LectureControllerV2(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(value = "", methods = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        Model model = new Model().addAttribute("lectures", lectureService.getLectures());
        return new ModelAndView("v2/lecture-list", model);
    }

    @RequestMapping(value = "/edit", methods = RequestMethod.GET)
    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.valueOf(request.getParameter("id"));
        Model model = new Model().addAttribute("lecture", lectureService.getLecture(id));
        return new ModelAndView("v2/lecture-edit", model);
    }

    @RequestMapping(value = "", methods = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        lectureService.saveLecture(title);
        return new ModelAndView("redirect:/v2/lectures", new Model());
    }

    @RequestMapping(value = "", methods = RequestMethod.PUT)
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.valueOf(request.getParameter("id"));
        String title = request.getParameter("title");
        Lecture lecture = lectureService.getLecture(id);
        lecture.setTitle(title);
        lectureService.updateLecture(lecture);
        return new ModelAndView("redirect:/v2/lectures", new Model());
    }

    @RequestMapping(value = "", methods = RequestMethod.DELETE)
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) {
        Long id = Long.valueOf(request.getParameter("id"));
        lectureService.deleteLecture(id);
        return new ModelAndView("redirect:/v2/lectures", new Model());
    }
}
