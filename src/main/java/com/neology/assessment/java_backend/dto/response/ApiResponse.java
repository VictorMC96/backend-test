package com.neology.assessment.java_backend.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResponse implements Serializable {
    private String businessMessage;
    private String message;
    private int httpStatus;

    public ApiResponse(String message, String businessMessage, int httpStatus) {
        this.message = message;
        this.businessMessage = businessMessage;
        this.httpStatus = httpStatus;
    }
}
