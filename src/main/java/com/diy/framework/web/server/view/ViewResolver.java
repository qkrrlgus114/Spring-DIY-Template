package com.diy.framework.web.server.view;

public class ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";

    public View resolveViewName(String viewName) {
        if (viewName == null) return null;

        if (viewName.startsWith(REDIRECT_PREFIX)) {
            return new RedirectView(viewName.substring(REDIRECT_PREFIX.length()));
        }

        String[] nameArr = viewName.split("\\.");
        if (nameArr.length == 1) {
            return null;
        }

        if (nameArr[1].equals("jsp")) {
            return new JspView("/" + viewName);
        } else if (nameArr[1].equals("html")) {
            return new HtmlView("/" + viewName);
        }

        return null;
    }

}
