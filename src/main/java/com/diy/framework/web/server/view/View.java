package com.diy.framework.web.server.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface View {

    void render(final Map<String, Object> model, final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException;
}
