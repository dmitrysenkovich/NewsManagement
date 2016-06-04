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


    /**
     * Writes object in the
     * response output stream.
     * @param response response.
     * @param object object to be
     * written in the response.
     * @throws IOException
     */
    public void write(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("text/json");
        PrintWriter out = response.getWriter();
        objectMapper.writeValue(out, object);
    }
}
