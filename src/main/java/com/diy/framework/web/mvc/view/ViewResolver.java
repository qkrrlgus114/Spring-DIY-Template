package com.diy.framework.web.mvc.view;

public interface ViewResolver {
    View resolveViewName(String viewName);
}