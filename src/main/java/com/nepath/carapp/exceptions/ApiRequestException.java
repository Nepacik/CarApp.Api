package com.nepath.carapp.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ApiRequestException extends RuntimeException {
    protected HttpStatus httpStatus;

    public ApiRequestException(String message) {
        super(message);
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public static final class AuthorizationException extends ApiRequestException {
        public AuthorizationException() {
            super("Authorization Error");
            this.httpStatus = HttpStatus.UNAUTHORIZED;
        }
    }

    public static final class BadRequestException extends ApiRequestException {
        public BadRequestException(String message) {
            super(message);
            this.httpStatus = HttpStatus.BAD_REQUEST;
        }
    }

    public static final class ServerErrorException extends ApiRequestException {
        public ServerErrorException() {
            super("Server Error");
            this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public static final class NotFoundErrorException extends ApiRequestException {
        public NotFoundErrorException(String message) {
            super(message);
            this.httpStatus = HttpStatus.NOT_FOUND;
        }
    }

    public static final class ConflictException extends ApiRequestException {
        public ConflictException(String message) {
            super(message);
            this.httpStatus = HttpStatus.CONFLICT;
        }
    }
}
