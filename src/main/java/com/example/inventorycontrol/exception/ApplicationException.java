package com.example.inventorycontrol.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@SuppressWarnings("squid:S1068")
public class ApplicationException extends RuntimeException {
    private final HttpStatus status;

    public ApplicationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
