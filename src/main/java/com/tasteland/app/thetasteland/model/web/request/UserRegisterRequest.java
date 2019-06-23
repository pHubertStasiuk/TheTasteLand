package com.tasteland.app.thetasteland.model.web.request;

import com.tasteland.app.thetasteland.utils.validators.FieldMatch;
import com.tasteland.app.thetasteland.utils.validators.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch.List({
        @FieldMatch(first = "encryptedPassword", second = "matchingPassword", message = "The encryptedPassword fields must match")
})
@Data
public class UserRegisterRequest {

    @ValidEmail
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private final String email;
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private final String firstName;
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private final String lastName;
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private final String password;
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private final String matchingPassword;
    @Size(min = 1, message = "is required")
    private final String pictureURL;
}
