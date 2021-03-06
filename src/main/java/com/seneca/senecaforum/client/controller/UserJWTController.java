package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.security.jwt.TokenProvider;
import com.seneca.senecaforum.service.UserService;
import com.seneca.senecaforum.service.constants.ApplicationConstants;
import com.seneca.senecaforum.service.dto.LoginRequest;
import com.seneca.senecaforum.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/")
public class UserJWTController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;


    @PostMapping("/auth")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest auth) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword())
            );
            final String jwt = tokenProvider.createToken(authentication, auth.getIsRememberMe());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(ApplicationConstants.AUTHORIZATION_HEADER, "Bearer " + jwt);
            UserDto userDto = userService.getUserByEmail(authentication.getName());
            userDto.setIsRememberMe(auth.getIsRememberMe());
            return new ResponseEntity<>(
                    userDto,
                    httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("Email and Password is not matched!");
        }
    }

}
