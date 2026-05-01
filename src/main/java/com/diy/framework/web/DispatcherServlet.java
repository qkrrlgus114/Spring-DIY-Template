package com.diy.framework.web;

import com.diy.framework.context.HandlerMethod;
import com.diy.framework.context.RestController;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.HandlerMapping;
import com.diy.framework.web.mvc.json.JsonResponseWriter;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

/**
 * Front Controller 역할
 * - 모든 HTTP 요청을 가장 먼저 받아 공통 작업을 처리한 후 적절한 컨트롤러로 요청을 위임.
 */
public class DispatcherServlet extends HttpServlet {

    private HandlerMapping handlerMapping;
    private final ViewResolver viewResolver = new JspViewResolver();
    private final JsonResponseWriter jsonResponseWriter = new JsonResponseWriter();

    @Override
    public void init() throws ServletException {
        try {
            BeanFactory beanFactory = new BeanFactory("com.diy");

            this.handlerMapping = new HandlerMapping();
            handlerMapping.initialize(beanFactory.getBeans());
        } catch (Exception e) {
            throw new ServletException("Framework Initialization Failed.", e);
        }
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        Object handler = handlerMapping.getHandler(req);

        if (handler == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        execute(handler, req, resp);
    }

    private void execute(Object handler, HttpServletRequest req, HttpServletResponse resp) throws ServletException, UnsupportedEncodingException {
        try {
            // 한글 인코딩
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json; charset=UTF-8");

            if (handler instanceof HandlerMethod handlerMap) {
                Object controller = ((HandlerMethod) handler).getBean();
                Class<?> controllerClass = controller.getClass();
                Method method = ((HandlerMethod) handler).getMethod();

                // Reflection 으로 메서드 실행
                Object result = method.invoke(controller, req, resp);

                if (controllerClass.isAnnotationPresent(RestController.class)) {
                    jsonResponseWriter.write(resp, result);
                } else if (result instanceof ModelAndView modelAndView) {
                    viewResolver.resolveViewName(modelAndView.getViewName()).render(req, resp, modelAndView.getModel());
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
