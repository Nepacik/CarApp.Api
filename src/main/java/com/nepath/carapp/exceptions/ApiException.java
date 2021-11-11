package com.nepath.carapp.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Getter
public class ApiException {

    private final String message;
    private final int code;
    private final String timestamp;

    public ApiException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(System.currentTimeMillis()));
        this.code = httpStatus.value();
    }
}
