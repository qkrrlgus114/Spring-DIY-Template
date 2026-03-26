package com.diy.framework.web.mvc.view;

import com.diy.framework.web.mvc.Model;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class HtmlView implements View {
    private final String viewName;

    public HtmlView(final String viewName) {
        this.viewName = viewName;
    }

    public void render(final HttpServletRequest req, final HttpServletResponse res, Model model) throws IOException {
        final String viewFile = readViewFile(req);

        res.setContentType("text/html;charset=utf-8");
        final PrintWriter writer = res.getWriter();
        writer.print(viewFile);
    }

    private String readViewFile(final HttpServletRequest request) {
        final StringBuilder content = new StringBuilder();
        final String viewPath = getViewPath(request);

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(viewPath), StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }

    private String getViewPath(final HttpServletRequest request) {
        final ServletContext sc = request.getServletContext();
        return sc.getRealPath(viewName);
    }
}
