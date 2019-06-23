package com.tasteland.app.thetasteland.service;

import com.tasteland.app.thetasteland.shared.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    UserDTO save(UserDTO userDTO);
    Optional<UserDTO> getByEmail(String email);
    Optional<UserDTO> getByUserId(String userId);
    void deleteById(String userId);




}
