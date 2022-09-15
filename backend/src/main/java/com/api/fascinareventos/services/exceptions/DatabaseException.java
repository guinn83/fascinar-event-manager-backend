package com.api.fascinareventos.services.exceptions;

import org.springframework.http.HttpStatus;

public class DatabaseException extends RuntimeException {

    public static HttpStatus status;

    public DatabaseException(String message) {
        super(message);
    }
}
