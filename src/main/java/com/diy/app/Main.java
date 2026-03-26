package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        // 소켓
        // 웹소켓 -> 양방향 통신할 때 사용하는 프로토콜 웹소켓 (소켓이랑 다름)
        // 모든 통신은 소켓열고 한다.
        // port 를 하나 여는 게 = 소켓을 하나 연다.

        TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
