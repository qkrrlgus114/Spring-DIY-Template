package com.diy.framework.web.server;

import com.diy.app.lecture.LectureController;
import com.diy.app.lecture.LectureService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 모든 요청은 DispatcherServlet이 받는다.
 * <p>
 * 따라서 각 요청에 따라 적절한 Servlet으로 분배를 시켜줘야 한다.
 *
 */
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private static final Map<String, Controller> httpServletMap = new HashMap<>();

    public DispatcherServlet() {
        httpServletMap.put("lectures", new LectureController(new LectureService()));
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String[] parts = requestURI.split("/");

        if (parts.length < 2) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String domainPath = parts[1];
        Controller controller = httpServletMap.get(domainPath);

        if (controller == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String pathInfo = requestURI.substring(("/" + domainPath).length());
        if (pathInfo.isEmpty()) {
            pathInfo = "/";
        }
        req.setAttribute("pathInfo", pathInfo);

        try {
            controller.handleRequest(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}

