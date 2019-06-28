package com.tasteland.app.thetasteland.utils;

public enum HttpMessage {

    MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields!"),
    RECORD_ALREADY_EXISTS("Record already exists"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    NO_RECORD_FOUND("Record with provided id is not found"),
    AUTHENTICATION_FAILED("Authentication failed"),
    AUTHENTICATION_SUCCESS("Authentication success"),
    COULD_NOT_UPDATED_RECORD("Could not update record"),
    COULD_NOT_DELETE_RECORD("Could not delete record"),
    EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified"),
    USER_REGISTRATION_FAILED("User registration failed"),
    USER_REGISTRATION_SUCCESSFUL("User has been registered successfully!"),
    USER_LOGOUT_FAILED("User is not authenticated!"),
    USER_LOGOUT_SUCCESSFUL("User has been logout!"),
    USER_LOGIN_FAILED("User is not logged in!"),
    USER_LOGIN_SUCCESSFUL("User has been logged in!"),
    USER_DELETED_SECCESSFUL("User has been deleted!");



    private String message;

    HttpMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
