package com.tasteland.app.thetasteland.model.web.request;

import lombok.Data;

@Data
public class UserLoginRequest {

    private final String email;
    private final String password;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

