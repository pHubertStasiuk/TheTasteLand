package com.tasteland.app.thetasteland.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -59857747523495955L;
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String userId;
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 120)
    private String email;
    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;
    @Column(name = "email_token", nullable = false)
    private String emailVerificationToken;
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;
}
