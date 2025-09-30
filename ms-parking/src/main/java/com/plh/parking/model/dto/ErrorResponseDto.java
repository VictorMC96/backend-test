package com.plh.parking.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponseDto {

    private String message;

    private String code;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    private Map<String, String> errors;

    public ErrorResponseDto(String message, String code) {
        this(message, code, null);
    }

    public ErrorResponseDto(String message, String code, Map<String, String> errors) {
        this.message = message;
        this.code = code;
        this.dateTime = LocalDateTime.now();
        this.errors = errors;
    }

}
