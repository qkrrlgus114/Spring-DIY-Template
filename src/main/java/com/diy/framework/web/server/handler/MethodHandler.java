package com.diy.framework.web.server.handler;

import com.diy.framework.web.server.model.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodHandler {

    private final Object instance;

    private final Method method;

    public MethodHandler(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public ModelAndView invoke(HttpServletRequest req, HttpServletResponse res) throws InvocationTargetException, IllegalAccessException {
        return (ModelAndView) method.invoke(instance, req, res);
    }
}
