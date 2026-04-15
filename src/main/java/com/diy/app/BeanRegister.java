package com.diy.app;

import com.diy.app.lecture.controller.LectureController;
import com.diy.app.lecture.repository.LectureRepository;
import com.diy.app.lecture.service.LectureService;
import com.diy.framework.web.server.bean.Bean;
import com.diy.framework.web.server.bean.Configuration;
import com.diy.framework.web.server.controller.Controller;

@Configuration
public class BeanRegister {

    @Bean("lectures")
    public Controller lectureController() {
        return new LectureController(new LectureService(new LectureRepository()));
    }
}
