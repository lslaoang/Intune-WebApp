package com.testco.intunewebapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class ResponseWriterUtil {
    private static final Logger LOGGER = Logger.getLogger(ResponseWriterUtil.class.getName());

    public static void writeErrorResponse(HttpServletResponse response, Object object) {
        try (PrintWriter writer = response.getWriter()) {
            String json = new ObjectMapper().writeValueAsString(object);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            LOGGER.severe("Error processing JSON. " + e.getMessage());
        }
    }
}
