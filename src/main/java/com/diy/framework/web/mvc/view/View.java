package com.diy.framework.web.mvc.view;

import com.diy.framework.web.mvc.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface View {
    void render(HttpServletRequest req, HttpServletResponse res, Model model) throws IOException, ServletException;
}
