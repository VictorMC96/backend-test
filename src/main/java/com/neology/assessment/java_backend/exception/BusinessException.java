package com.neology.assessment.java_backend.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private String businessMessage;
    public BusinessException(String message, String businessMessage)
    {
        super(message);
        this.businessMessage = businessMessage;
    }

}
