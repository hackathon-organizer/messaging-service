package com.hackathonorganizer.messagingservice.exception;


import org.springframework.http.HttpStatus;

public class SignalServerException extends RuntimeException {

    private HttpStatus httpStatus;

    public SignalServerException(String message) {
        super(message);
    }

    public SignalServerException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
