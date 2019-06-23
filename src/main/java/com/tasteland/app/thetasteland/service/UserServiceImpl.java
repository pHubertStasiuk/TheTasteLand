package com.tasteland.app.thetasteland.service;

import com.tasteland.app.thetasteland.entity.UserEntity;
import com.tasteland.app.thetasteland.repository.RoleRepository;
import com.tasteland.app.thetasteland.repository.UserRepository;
import com.tasteland.app.thetasteland.shared.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);
        userEntity.setAccountCreationDate(LocalDate.now());
        userEntity.setLastPasswordReset(LocalDate.now());
        userEntity.setRoles(Collections.singleton(roleRepository.findRoleByName("ROLE_USER")));
        userRepository.save(userEntity);
        BeanUtils.copyProperties(userEntity, userDTO);
        return userDTO;
    }

    @Override
    public Optional<UserDTO> getByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        UserDTO userDTO = new UserDTO();
        if (user != null) {
            BeanUtils.copyProperties(user, userDTO);
            return Optional.of(userDTO);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDTO> getByUserId(String userId) {

        UserEntity userFromDB = userRepository.findByUserId(userId);
        Optional<UserEntity> user = Optional.of(userFromDB);
        Optional<UserDTO> userDTO = Optional.of(new UserDTO());
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public void deleteById(String userId) {
        UserEntity userEntity;
        if ((userEntity = userRepository.findByUserId(userId)) != null) userRepository.delete(userEntity);
    }
}
