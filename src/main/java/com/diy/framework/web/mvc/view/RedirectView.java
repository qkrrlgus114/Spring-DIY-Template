package com.diy.framework.web.mvc.view;

import com.diy.framework.web.mvc.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RedirectView implements View {
    private final String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse res, Model model) throws IOException {
        res.sendRedirect(url);
    }
}
