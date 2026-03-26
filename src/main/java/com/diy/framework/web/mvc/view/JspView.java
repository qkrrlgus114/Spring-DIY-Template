package com.diy.framework.web.mvc.view;

import com.diy.framework.web.mvc.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JspView implements View {
    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    public void render(final HttpServletRequest req, final HttpServletResponse res, Model model) throws ServletException, IOException {
        model.getAttributes().forEach(req::setAttribute);
        req.getRequestDispatcher("/" + viewName).forward(req, res);
    }
}