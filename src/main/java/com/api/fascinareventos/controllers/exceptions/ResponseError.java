package com.api.fascinareventos.controllers.exceptions;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
public class ResponseError<T> {

    private String timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    private T data;

    private static final Logger logger = Logger.getLogger(String.valueOf(ResponseError.class));

    private ResponseError() {
    }

    public ResponseError(String timestamp, Integer status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ResponseError(String timestamp, Integer status, String error, String message, String path, T data) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.data = data;
    }

    private String toJson() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.log(Level.ALL, e.getLocalizedMessage());
            throw e;
        }
    }

    private void send(HttpServletResponse response, int code) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json");
        String errorMessage;

        errorMessage = toJson();

        response.getWriter().println(errorMessage);
        response.getWriter().flush();
    }

    public static void sendError(HttpServletResponse response, HttpServletRequest request, int code, String error, Exception e) throws IOException {
        SecurityContextHolder.clearContext();

        String instant = OffsetDateTime.now(ZoneId.systemDefault()).toString();

        ResponseError<String> exceptionResponse =
                new ResponseError<>(instant, code, error, e.getMessage(), request.getRequestURI());

        exceptionResponse.send(response, code);
    }
}
