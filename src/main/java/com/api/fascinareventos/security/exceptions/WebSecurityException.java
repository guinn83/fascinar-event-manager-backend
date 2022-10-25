package com.api.fascinareventos.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

public class WebSecurityException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 1L;
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public WebSecurityException(String message) {
        super(message);
    }

    public WebSecurityException(HttpStatus status, String message) {
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
