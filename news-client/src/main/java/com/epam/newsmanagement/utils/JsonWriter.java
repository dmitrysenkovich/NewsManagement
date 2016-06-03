package com.epam.newsmanagement.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Json writer. Simply writes
 * an object in the output stream.
 */
public class JsonWriter {
    @Autowired
    private ObjectMapper objectMapper;


    public void write(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("text/json");
        PrintWriter out = response.getWriter();
        objectMapper.writeValue(out, object);
    }
}
