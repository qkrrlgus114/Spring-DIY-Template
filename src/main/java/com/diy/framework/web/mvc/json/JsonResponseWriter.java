package com.diy.framework.web.mvc.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * Json 응답을 만들기 위한 클래스
 * */
public class JsonResponseWriter {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public void write(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        String json = objectMapper.writeValueAsString(object);
        response.getWriter().write(json);
    }
}
