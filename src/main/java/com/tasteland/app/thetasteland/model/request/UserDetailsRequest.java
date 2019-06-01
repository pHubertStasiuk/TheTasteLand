package com.tasteland.app.thetasteland.model.request;

import lombok.Data;

@Data
public class UserDetailsRequest {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

}
