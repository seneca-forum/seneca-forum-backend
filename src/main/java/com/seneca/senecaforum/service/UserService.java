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
import java.util.List;
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
        return userRepository.existsByUsername(usr);
    }

    public boolean isUserIdValid(String userId){
        return userRepository.existsByUserId(userId);
    }

    public boolean isEmailUnique(String email){
        return userRepository.existsByEmail(email);
    }

    public UserDto getUserByEmail(String email){
        UserEntity userEntity = userRepository.findOneWithAuthoritiesByEmail(email).orElse(null);
        return MapperUtils.mapperObject(userEntity,UserDto.class);
    }

    public UserDto saveUser(UserEntity usr){
        UserEntity savedUsr = UserEntity.builder()
                .createdOn(new Date())
                .email(usr.getEmail())
                .password(passwordEncoder.encode(usr.getPassword()))
                .role(usr.getRole())
                .username(usr.getUsername())
                .build();
        return MapperUtils.mapperObject(userRepository.save(savedUsr),UserDto.class);
    }

    public UserEntity getUserByUserId(String userId){
        return userRepository.findById(userId).get();
    }

    public List<UserEntity>getAllUsers(){return userRepository.findAll();};
}
