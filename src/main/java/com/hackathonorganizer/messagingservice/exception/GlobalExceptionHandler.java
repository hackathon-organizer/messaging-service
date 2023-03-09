package com.hackathonorganizer.messagingservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({MessagingException.class})
    public ResponseEntity<ErrorResponse> handleChatException(MessagingException ex) {

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), List.of(ex.getMessage()));

        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }
}
