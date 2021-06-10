package com.seneca.senecaforum.service;


import com.seneca.senecaforum.domain.UserEntity;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.security.jwt.TokenProvider;
import com.seneca.senecaforum.service.dto.UserDto;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserEntity getUserByUsername(String username){
        return userRepository.findByUsername(username).get();
    }

    public boolean isUsernameUnique(String usr){
        Optional<UserEntity>user = userRepository.findByUsername(usr);
        if(user.isPresent()){
            return false;
        }
        return true;
    }

    public UserDto getUserByEmail(String email){
        UserEntity userEntity = userRepository.findOneWithAuthoritiesByEmail(email).orElse(null);
        return MapperUtils.mapperObject(userEntity,UserDto.class);
    }

    public UserEntity saveUser(UserEntity usr){
        String rawPassword = usr.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        UserEntity savedUsr = UserEntity.builder()
                .createdOn(new Date())
                .email(usr.getEmail())
                .password(encodedPassword)
                .role(usr.getRole())
                .username(usr.getUsername())
                .build();
        return userRepository.save(savedUsr);
    }


    public String createNewToken(Authentication authentication, boolean rememberMe){
        return tokenProvider.createToken(authentication, rememberMe);
    }

    public boolean isUserIdValid(String userId){
        Optional<UserEntity>user = userRepository.findById(userId);
        if(user.isPresent()){
            return true;
        }
        return false;
    }
    public UserEntity getUserByUserId(String userId){
        return userRepository.findById(userId).get();
    }
}
