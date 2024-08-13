package com.otunba.medipro.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
public class ExceptionResponse {
    private String message;
    private HttpStatus httpStatus;
    private boolean flag;
    private ZonedDateTime timestamp;
}
