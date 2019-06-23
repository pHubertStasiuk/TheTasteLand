package com.tasteland.app.thetasteland.shared.dto;

import com.tasteland.app.thetasteland.utils.StringRandomize;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserDTO implements Serializable {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 2238157241346298603L;
    private Long id;
    private String userId = StringRandomize.generateUserId();
    private String firstName;
    private String lastName;
    private String email;
    private String encryptedPassword;
    private Boolean emailVerified = false;
    private String pictureURL;

}
