package com.api.fascinareventos.controllers.exceptions;

import com.api.fascinareventos.services.exceptions.DatabaseException;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import com.api.fascinareventos.security.exceptions.WebSecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler extends RuntimeException{

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<StandardError> handleAllExceptions(Exception e, HttpServletRequest request){
        StandardError err = new StandardError(Instant.now(),  HttpStatus.BAD_REQUEST.value(), "Exception error", e.getMessage(), request.getRequestURI());
        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
        String error = "Database error";
        StandardError err = new StandardError(Instant.now(), e.getStatus().value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(e.getStatus()).body(err);
    }

    @ExceptionHandler(WebSecurityException.class)
    public ResponseEntity<StandardError> securityError(WebSecurityException e, HttpServletRequest request) {
        String error = "Security error";
        StandardError err = new StandardError(Instant.now(), e.getStatus().value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(e.getStatus()).body(err);
    }
}
