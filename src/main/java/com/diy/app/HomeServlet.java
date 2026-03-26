package com.diy.app;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    // init(): 처음 호출 시 캐싱함.
    // service 호출 후 doGet() 호출됨.

    @Override
    public void init(final ServletConfig config) throws ServletException {
        System.out.println("init called.");
        super.init(config);
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("service called.");
        resp.getWriter().write("Home");
    }
}
