package com.plh.parking.commons.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.plh.parking.model.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ObjectMapper mapper = new ObjectMapper();

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleException(Exception ex) {
        log.error("Exception " + ex.getMessage(), ex);
        return new ErrorResponseDto(ex.getMessage(), "" + HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleClient(HttpClientErrorException ex) {
        log.error("HttpClientErrorException " + ex.getMessage(), ex);
        var json = convertJson(ex.getMessage());
        var error = new ErrorResponseDto(json.get("message").asText(), "" + ex.getStatusCode().value());

        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getStatusCode().value()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(ResourceNotFoundException ex) {
        log.error("ResourceNotFoundException " + ex.getMessage(), ex);
        var error = new ErrorResponseDto(ex.getMessage(), "" + ex.getHttpStatus().value());

        return new ResponseEntity<>(error, ex.getHttpStatus());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException " + ex.getMessage(), ex);

        final Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {
                    String field = error.getField();
                    field = ((PropertyNamingStrategies.SnakeCaseStrategy) PropertyNamingStrategies.SNAKE_CASE).translate(field);
                    String message = error.getDefaultMessage();
                    errors.put(field, message);
                });

        return new ErrorResponseDto("Validation failed for argument", "" + HttpStatus.BAD_REQUEST.value(), errors);

    }


    private JsonNode convertJson(String message) {
        try {
            if (Objects.isNull(message)) {
                return mapper.createObjectNode();
            }
            var messageTmp = message.split(":\"")[1];
            messageTmp = messageTmp.substring(0, messageTmp.length() - 1);
            return mapper.readTree(messageTmp);
        } catch (JsonProcessingException e) {
            return mapper.createObjectNode();
        }
    }

}
