package com.diy.framework.web.server.servlet;

import com.diy.framework.web.server.bean.BeanFactory;
import com.diy.framework.web.server.handler.AnnotationHandlerMapping;
import com.diy.framework.web.server.handler.BeanNameHandlerMapping;
import com.diy.framework.web.server.handler.ControllerHandlerAdapter;
import com.diy.framework.web.server.handler.HandlerAdapter;
import com.diy.framework.web.server.handler.HandlerMapping;
import com.diy.framework.web.server.handler.MethodHandlerAdapter;
import com.diy.framework.web.server.model.ModelAndView;
import com.diy.framework.web.server.view.View;
import com.diy.framework.web.server.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 모든 요청은 DispatcherServlet이 받는다.
 * <p>
 * 요청에 맞는 핸들러는 HandlerMapping 들에게 물어보고,
 * 핸들러를 실제 실행하는 건 HandlerAdapter 들에게 맡긴다.
 */
public class DispatcherServlet extends HttpServlet {

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private final ViewResolver viewResolver = new ViewResolver();
    private final BeanFactory beanFactory;

    public DispatcherServlet(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void init() {
        // 애너테이션 기반이 더 구체적이니까 먼저 물어본다.
        handlerMappings.add(new AnnotationHandlerMapping(beanFactory));
        handlerMappings.add(new BeanNameHandlerMapping(beanFactory));

        handlerAdapters.add(new MethodHandlerAdapter());
        handlerAdapters.add(new ControllerHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        Object handler = getHandler(req);
        if (handler == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        HandlerAdapter adapter = getAdapter(handler);
        if (adapter == null) {
            // 이럴 일은 없어야 하지만 방어적으로
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        try {
            ModelAndView mav = adapter.handle(req, resp, handler);
            render(mav, req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private Object getHandler(HttpServletRequest req) {
        for (HandlerMapping mapping : handlerMappings) {
            Object handler = mapping.getHandler(req);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    private HandlerAdapter getAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        return null;
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
