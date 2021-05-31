package com.seneca.senecaforum.security;

import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()){
            throw new IllegalArgumentException("Found nothing with this email "+ email);
        }
        return new CustomUserDetails(user.get());
    }

    public UserDetails loadUserByUserId(String id)throws UsernameNotFoundException{
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()){
            throw new IllegalArgumentException("Found nothing with this id "+ id);
        }
        return new CustomUserDetails(user.get());
    }
}
