package com.seneca.senecaforum.service;

import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).get();
    }
}
