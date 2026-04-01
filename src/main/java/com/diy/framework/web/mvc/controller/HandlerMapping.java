package com.diy.framework.web.mvc.controller;

import com.diy.app.LectureController;
import com.diy.app.LectureRepository;
import com.diy.app.LectureService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {

    private final Map<String, Controller> mapper = new HashMap<>();
    
    public HandlerMapping() {
        LectureRepository lectureRepository = new LectureRepository();
        LectureService lectureService = new LectureService(lectureRepository);

        mapper.put("GET /lectures", new LectureController(lectureService));
        mapper.put("POST /lectures", new LectureController(lectureService));
        mapper.put("PUT /lectures", new LectureController(lectureService));
        mapper.put("DELETE /lectures", new LectureController(lectureService));
        mapper.put("GET /lectures/edit", new LectureController(lectureService));
    }

    public void initialize(Map<Class<?>, Object> beans) {
        for (Object bean : beans.values()) {
            if (bean instanceof Controller) {
                Controller controller = (Controller) bean;

                String url = "/maping-url"; //temp
                mapper.put(url, controller);
                System.out.println("[HandlerMapping] " + url + "-> " + bean.getClass().getName());
            }
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String key = request.getMethod() + " " + request.getRequestURI();
        return mapper.get(key);
    }
}
