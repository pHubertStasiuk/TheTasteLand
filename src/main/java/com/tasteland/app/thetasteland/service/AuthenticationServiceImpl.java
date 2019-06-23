package com.tasteland.app.thetasteland.service;

import com.tasteland.app.thetasteland.entity.UserEntity;
import com.tasteland.app.thetasteland.model.web.response.HttpResponse;
import com.tasteland.app.thetasteland.repository.UserRepository;
import com.tasteland.app.thetasteland.utils.HttpMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        } else {
            return new ResponseEntity<>(new HttpResponse(HttpMessage.USER_LOGOUT_FAILED), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new HttpResponse(HttpMessage.USER_LOGOUT_SUCCESSFUL), HttpStatus.OK);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Invalid user credentials");
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), userEntity.getRoles());
    }
}
