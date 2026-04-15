package com.diy.framework.web.server.servlet;

import com.diy.framework.web.server.bean.BeanFactory;
import com.diy.framework.web.server.controller.Controller;
import com.diy.framework.web.server.model.ModelAndView;
import com.diy.framework.web.server.view.View;
import com.diy.framework.web.server.view.ViewResolver;

import javax.servlet.ServletException;
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
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> httpServletMap = new HashMap<>();
    private final ViewResolver viewResolver = new ViewResolver();
    private final BeanFactory beanFactory;

    public DispatcherServlet(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void init() {
        httpServletMap.putAll(beanFactory.getBeansOfType(Controller.class));
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
            ModelAndView mav = controller.handleRequest(req, resp);
            render(mav, req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        if (mav == null) {
            return;
        }

        final String viewName = mav.getViewName();
        final View view = viewResolver.resolveViewName(viewName);

        if (view == null) {
            throw new ServletException("View not found: " + viewName);
        }

        view.render(mav.getModel(), req, resp);
    }

}
