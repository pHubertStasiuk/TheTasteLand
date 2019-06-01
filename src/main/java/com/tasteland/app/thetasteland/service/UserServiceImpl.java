package com.tasteland.app.thetasteland.service;

import com.tasteland.app.thetasteland.entity.UserEntity;
import com.tasteland.app.thetasteland.repository.UserRepository;
import com.tasteland.app.thetasteland.shared.UserDTO;
import com.tasteland.app.thetasteland.shared.UserAlreadyExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null)
            throw new UserAlreadyExistsException("User already exists!");
        UserEntity userEntity = UserEntity.builder()
                .userId(userDTO.getUserId())
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .emailVerificationToken(userDTO.getEmailVerificationToken())
                .encryptedPassword(userDTO.getEncryptedPassword())
                .emailVerified(userDTO.getEmailVerified())
                .build();
        UserEntity storedUser = userRepository.save(userEntity);
        UserDTO user = new UserDTO();
        BeanUtils.copyProperties(storedUser, user);
        return user;
    }
}
