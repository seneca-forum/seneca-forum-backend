package com.seneca.senecaforum.service;


import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).get();
    }

    public boolean isUsernameUnique(String usr){
        Optional<User>user = userRepository.findByUsername(usr);
        if(user.isPresent()){
            return false;
        }
        return true;
    }

    public boolean isEmailUnique(String email){
        Optional<User>user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return false;
        }
        return true;
    }

    public User saveUser(User usr){
        String rawPassword = usr.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User savedUsr = new User().builder()
                .createdOn(new Date())
                .email(usr.getEmail())
                .password(encodedPassword)
                .role(usr.getRole())
                .isRememberMe(false)
                .username(usr.getUsername())
                .build();
        return userRepository.save(savedUsr);

    }

}
