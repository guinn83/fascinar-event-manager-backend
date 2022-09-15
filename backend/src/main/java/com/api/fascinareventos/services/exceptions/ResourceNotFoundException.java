package com.api.fascinareventos.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Object id, String message) {
        super("id: " + id + " -> " + message);
    }
}
