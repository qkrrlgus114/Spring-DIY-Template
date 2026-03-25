package com.diy.framework.web.server.view;

import com.diy.framework.web.server.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JspView implements View {
    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    public void render(final HttpServletRequest req, final HttpServletResponse res, Model model) throws ServletException, IOException {
        for (Map.Entry<String, Object> entry : model.getAll().entrySet()) {
            req.setAttribute(entry.getKey(), entry.getValue());
        }

        final RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        requestDispatcher.forward(req, res);
    }
}
