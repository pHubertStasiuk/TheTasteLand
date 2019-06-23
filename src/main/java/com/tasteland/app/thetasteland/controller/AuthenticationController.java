package com.tasteland.app.thetasteland.controller;


import com.tasteland.app.thetasteland.model.web.request.UserLoginRequest;
import com.tasteland.app.thetasteland.model.web.response.HttpResponse;
import com.tasteland.app.thetasteland.service.AuthenticationService;
import com.tasteland.app.thetasteland.utils.HttpMessage;
import com.tasteland.app.thetasteland.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService,
                                    AuthenticationManager authenticationManager,
                                    JwtUtils jwtUtils) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    @PostMapping(
            path = "/token",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> generateToken(@RequestBody UserLoginRequest user) {
        final String email = user.getEmail();
        this.authenticate(email, user.getPassword());
        final UserDetails userDetails = authenticationService.loadUserByUsername(email);
        final String token = jwtUtils.generateToken(userDetails);
        return new ResponseEntity<>(new HttpResponse(HttpMessage.AUTHENTICATION_SUCCESS, token), HttpStatus.OK);
    }

    private Authentication authenticate(String email, String password) {
        if (Objects.nonNull(email) && Objects.nonNull(password)) {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } else {
            throw new UsernameNotFoundException("User has not been found!");
        }
    }
    @PostMapping(
            path = "/login",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest) {

        Authentication authentication;
        try {
            authentication = authenticate(userLoginRequest.getEmail(), userLoginRequest.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = authenticationService.loadUserByUsername(userLoginRequest.getEmail());
            final String token = jwtUtils.generateToken(userDetails);
            return new ResponseEntity<>(
                    new HttpResponse(HttpMessage.USER_LOGIN_SUCCESSFUL, token), HttpStatus.OK);
        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            return new ResponseEntity<>(
                    new HttpResponse(HttpMessage.USER_LOGIN_FAILED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return authenticationService.logout(request, response);
    }


}
