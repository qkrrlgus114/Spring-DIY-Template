package com.diy.framework.web.server;

import com.diy.app.global.Domain;
import com.diy.app.lecture.LectureServlet;

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

    private static final Map<String, HttpServlet> httpServletMap = new HashMap<>();

    public DispatcherServlet() {
        httpServletMap.put(Domain.LECTURES.name(), new LectureServlet());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        // reqBody는 한 번만 읽을 수 있다네요? 그래서 아래 제거했습니다.
//        final Map<String, ?> params = parseParams(req);

        String uri = req.getRequestURI();

        // favicon 요청은 무시(디버그 찍어보면 /favicon.ico가 요청으로 들어옴)
        if (uri.equals("/favicon.ico")) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        // 앞에 도메인을 확인
        String[] splitDomainUrl = uri.split("/");
        // 길이가 1이고 / 요청이면 루트로 판단
        if (splitDomainUrl.length == 1 && splitDomainUrl[0].equals("/")) {
            return;
        }
        // 길이가 1이면 잘못된 요청
        else if (splitDomainUrl.length == 1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 도메인에 맞는 서블릿 판단
        HttpServlet httpServlet = httpServletMap.get(splitDomainUrl[1]);
        if (httpServlet == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 도메인 이후 경로를 pathInfo로 전달 (예: /lectures/register → /register)
        String domain = "/" + splitDomainUrl[1];
        String pathInfo = uri.substring(domain.length());
        req.setAttribute("pathInfo", pathInfo.isEmpty() ? "/" : pathInfo);

        try {
            httpServlet.service(req, resp);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

}

