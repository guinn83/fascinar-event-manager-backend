package com.api.fascinareventos.services.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class DatabaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(HttpStatus status, String message) {
        super(message);
        setStatus(status);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
