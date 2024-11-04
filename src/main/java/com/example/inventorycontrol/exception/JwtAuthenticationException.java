package com.example.inventorycontrol.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@SuppressWarnings({"squid:S1165", "squid:S1186", "squid:S1068", "squid:S1948"})
public class JwtAuthenticationException extends RuntimeException {

    private HttpStatus httpStatus;
    private Object errors;

    public JwtAuthenticationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public JwtAuthenticationException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public JwtAuthenticationException(String tokenIsExpired) {
    }

}