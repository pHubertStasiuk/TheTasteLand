package com.tasteland.app.thetasteland.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationService extends UserDetailsService {

    ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);

}
