package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.BadRequestException;
import com.seneca.senecaforum.client.exception.InternalException;
import com.seneca.senecaforum.client.exception.NotFoundException;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Role;
import com.seneca.senecaforum.domain.UserEntity;
import com.seneca.senecaforum.service.PostService;
import com.seneca.senecaforum.service.RoleService;
import com.seneca.senecaforum.service.UserService;
import com.seneca.senecaforum.service.constants.ApplicationConstants;
import com.seneca.senecaforum.service.dto.PostViewDto;
import com.seneca.senecaforum.service.dto.UserDto;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping()
    public ResponseEntity<UserDto> createNewUser(@RequestBody UserEntity usr) throws URISyntaxException {
        if (userService.isUsernameUnique(usr.getUsername())) {
            throw new BadRequestException("Username already exists!");
        }
        if (userService.isEmailUnique(usr.getEmail())) {
            throw new BadRequestException("Email already exists!");
        }
        usr.setRole(roleService.getRoleByRoleName("ROLE_USER"));
        return ResponseEntity.created(
                new URI(ApplicationConstants.BASE_URL+"/users/"+usr.getUserId()))
                .body(userService.saveUser(usr));
    }


    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable("userId")  String userId) {
        if(!userService.isUserIdValid(userId)){
            throw new NotFoundException("Cannot find any users with userId "+userId);
        }
        UserDto usr = MapperUtils.mapperObject(userService.getUserByUserId(userId),UserDto.class);
        return ResponseEntity.ok(usr);
    }
}
