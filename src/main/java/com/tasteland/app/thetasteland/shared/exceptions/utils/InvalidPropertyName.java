package com.tasteland.app.thetasteland.shared.exceptions.utils;

public class InvalidPropertyName extends RuntimeException {

    public InvalidPropertyName(String message) {
        super(message);
    }

    public InvalidPropertyName(String message, Throwable cause) {
        super(message, cause);
    }
}
