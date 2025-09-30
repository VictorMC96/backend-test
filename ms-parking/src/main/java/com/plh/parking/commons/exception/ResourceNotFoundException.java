package com.plh.parking.commons.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -4134518317661347550L;
	private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
