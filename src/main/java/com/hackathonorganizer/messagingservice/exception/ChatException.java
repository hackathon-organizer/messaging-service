package com.hackathonorganizer.messagingservice.exception;

import org.springframework.http.HttpStatus;

public class ChatException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ChatException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
