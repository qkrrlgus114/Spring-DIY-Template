package com.diy.framework.web.mvc;

public class ModelAndView {
    private final String viewName;
    private final Model model;

    public ModelAndView(String viewName, Model model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Model getModel() {
        return model;
    }
}
