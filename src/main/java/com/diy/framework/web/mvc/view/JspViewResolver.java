package com.diy.framework.web.mvc.view;

public class JspViewResolver implements ViewResolver {
    private final String prefix;
    private final String suffix;

    public JspViewResolver() {
        this.prefix = "";
        this.suffix = ".jsp";
    }

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith("redirect:")) {
            return new RedirectView(viewName.substring("redirect:".length()));
        }
        return new JspView(prefix + viewName + suffix);
    }
}
