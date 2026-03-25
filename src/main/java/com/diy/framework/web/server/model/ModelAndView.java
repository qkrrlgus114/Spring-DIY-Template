package com.diy.framework.web.server.model;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private final String viewName;
    private final Map<String, Object> model = new HashMap<>();

    public ModelAndView(final String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(final String viewName, final Map<String, Object> model) {
        this.viewName = viewName;
        this.model.putAll(model);
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return this.model;
    }
}
