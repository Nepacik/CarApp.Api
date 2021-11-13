package com.nepath.carapp.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException apiRequestException) {
        ApiException apiException = new ApiException(apiRequestException.getMessage(), apiRequestException.getHttpStatus());
        return new ResponseEntity<>(apiException, apiRequestException.getHttpStatus());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Optional<String> errorMessage = methodArgumentNotValidException.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).findFirst();
        String message = errorMessage.orElse("Bad Request");
        ApiException apiException = new ApiException(message, httpStatus);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {CannotCreateTransactionException.class})
    public ResponseEntity<Object> handleDatabaseConnectionError(CannotCreateTransactionException cannotCreateTransactionException) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException("Server Error", httpStatus);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {JWTVerificationException.class})
    public ResponseEntity<Object> handleJTWVerivicationException(JWTVerificationException jwtVerificationException) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        ApiException apiException = new ApiException("Authorization Error", httpStatus);
        return new ResponseEntity<>(apiException, httpStatus);
    }
}