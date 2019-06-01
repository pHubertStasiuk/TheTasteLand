package com.tasteland.app.thetasteland.model.response;

import lombok.Data;

@Data
public class UserDetailsResponse {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
}
