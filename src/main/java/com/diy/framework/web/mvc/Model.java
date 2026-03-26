package com.diy.framework.web.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Model {
    private final Map<String, Object> attributes = new HashMap<>();

    public Model addAttribute(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}
