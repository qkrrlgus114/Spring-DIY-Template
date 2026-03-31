package com.diy.app;

import com.diy.framework.web.DispatcherServlet;
import com.diy.framework.web.server.TomcatWebServer;

/*
 소켓
 웹소켓 -> 양방향 통신할 때 사용하는 프로토콜 웹소켓 (소켓이랑 다름)
 모든 통신은 소켓열고 한다.
 port 를 하나 여는 게 = 소켓을 하나 연다.
 */
public class Main {
    public static void main(String[] args) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        TomcatWebServer tomcatWebServer = new TomcatWebServer(dispatcherServlet);
        tomcatWebServer.start();
    }
}
