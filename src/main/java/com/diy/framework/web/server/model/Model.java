package com.diy.framework.web.server.model;

import java.util.HashMap;
import java.util.Map;

/*
 * 계층별로 데이터를 옮기기 위한 모델!!!
 * */
public class Model {

    private Map<String, Object> data;

    public Model() {
        this.data = new HashMap<>();
    }

    public void setData(String key, Object data) {
        this.data.put(key, data);
    }

    public Object getValue(String key) {
        return data.get(key);
    }

    public Map<String, Object> getAll() {
        return data;
    }


}
