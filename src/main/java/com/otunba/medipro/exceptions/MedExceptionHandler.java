package com.otunba.medipro.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class MedExceptionHandler {

    @ExceptionHandler(value = {AuthException.class})
    public ResponseEntity<?> handleAuthException(AuthException ex) {
        var response = new ExceptionResponse();
        response.setTimestamp(ZonedDateTime.now(ZoneId.of("Z")));
        response.setMessage(ex.getMessage());
        response.setFlag(false);
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(response);
    }
}
