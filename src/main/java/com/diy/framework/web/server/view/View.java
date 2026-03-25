package com.diy.framework.web.server.view;

import com.diy.framework.web.server.model.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface View {

    void render(final HttpServletRequest req, final HttpServletResponse res, Model model) throws IOException, ServletException;
}
