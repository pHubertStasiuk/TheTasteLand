package com.tasteland.app.thetasteland.controller;


import com.tasteland.app.thetasteland.model.web.request.UserRegisterRequest;
import com.tasteland.app.thetasteland.model.web.response.HttpResponse;
import com.tasteland.app.thetasteland.model.web.response.UserDetailsResponse;
import com.tasteland.app.thetasteland.service.UserService;
import com.tasteland.app.thetasteland.shared.dto.UserDTO;
import com.tasteland.app.thetasteland.utils.HttpMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }


    @InitBinder
    public void initUserRequest(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }


    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> create(@Valid @RequestBody UserRegisterRequest userDetailsRequest,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new HttpResponse(HttpMessage.USER_REGISTRATION_FAILED), HttpStatus.EXPECTATION_FAILED);
        if (userDetailsRequest.getEmail().isEmpty())
            return new ResponseEntity<>(new HttpResponse(HttpMessage.MISSING_REQUIRED_FIELD), HttpStatus.BAD_REQUEST);

        Optional<UserDTO> existing = userService.getByEmail(userDetailsRequest.getEmail());
        if (existing.isPresent())
            return new ResponseEntity<>(new HttpResponse(HttpMessage.RECORD_ALREADY_EXISTS), HttpStatus.FORBIDDEN);

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDetailsRequest, userDTO);
        userDTO.setEncryptedPassword(encoder.encode(userDetailsRequest.getPassword()));
        UserDTO user = userService.save(userDTO);
        UserDetailsResponse response = new UserDetailsResponse();
        BeanUtils.copyProperties(user, response);
        return new ResponseEntity<>(new HttpResponse(HttpMessage.USER_REGISTRATION_SUCCESSFUL, response), HttpStatus.OK);
    }
}
