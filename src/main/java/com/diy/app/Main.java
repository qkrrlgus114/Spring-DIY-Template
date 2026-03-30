package com.diy.app;

import com.diy.framework.web.DispatcherServlet;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.server.TomcatWebServer;

import java.util.HashMap;
import java.util.Map;

/*
 소켓
 웹소켓 -> 양방향 통신할 때 사용하는 프로토콜 웹소켓 (소켓이랑 다름)
 모든 통신은 소켓열고 한다.
 port 를 하나 여는 게 = 소켓을 하나 연다.
 */
public class Main {
    public static void main(String[] args) throws Exception {

        BeanFactory beanFactory = new BeanFactory("com.diy");

        LectureController lectureController = beanFactory.getBean(LectureController.class);

        Map<String, Controller> handlerMap = new HashMap<>();
        handlerMap.put("GET /lectures", lectureController);
        handlerMap.put("POST /lectures", lectureController);
        handlerMap.put("PUT /lectures", lectureController);
        handlerMap.put("DELETE /lectures", lectureController);
        handlerMap.put("GET /lectures/edit", lectureController);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMap);

        TomcatWebServer tomcatWebServer = new TomcatWebServer(dispatcherServlet);
        tomcatWebServer.start();
    }
}
