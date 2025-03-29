package com.devconnect.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forResource(String resource, String id) {
        return new ResourceNotFoundException(String.format("%s with id %s not found", resource, id));
    }
}
