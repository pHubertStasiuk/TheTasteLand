package com.tasteland.app.thetasteland.shared.exceptions.utils;

public class InvalidPropertyFormatException extends RuntimeException {

    public InvalidPropertyFormatException(String message) {
        super(message);
    }

    public InvalidPropertyFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
