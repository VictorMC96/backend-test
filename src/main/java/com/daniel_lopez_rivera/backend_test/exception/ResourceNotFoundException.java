package com.daniel_lopez_rivera.backend_test.exception;

public class ResourceNotFoundException extends RuntimeException {
    private String message;
    public ResourceNotFoundException(String message){
        super(message);
        this.message = message;
    }
}
