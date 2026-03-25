package com.diy.framework.web.server.view;

public class ViewResolver {


    public View resolveViewName(String viewName) {
        if (viewName == null) return null;

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
