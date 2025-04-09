package com.neology.assessment.java_backend.handler;

import com.neology.assessment.java_backend.dto.response.ApiResponse;
import com.neology.assessment.java_backend.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle ResourceNotFoundException globally
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Status code 404
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException ex) {
        ApiResponse apiResponse = new ApiResponse(ex.getMessage(), ex.getBusinessMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Status code 500
    public ResponseEntity<ApiResponse> handleException(Exception ex) {
        ApiResponse apiResponse = new ApiResponse(ex.getMessage(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // You can add more exception handlers for different custom exceptions or other built-in exceptions (e.g., IllegalArgumentException)
}

