package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.BadRequestException;
import com.seneca.senecaforum.client.exception.InternalException;
import com.seneca.senecaforum.domain.Role;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.service.RoleService;
import com.seneca.senecaforum.service.UserService;
import com.seneca.senecaforum.service.dto.UserDto;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/new")
    public ResponseEntity<UserDto> createNewUser(@RequestBody User usr){
        Role role = roleService.getRoleByRoleName(usr.getRole().getRoleName());
        if(role == null){
            throw new BadRequestException("Rolename "+usr.getRole().getRoleName()+"does not exist!");
        }
        if(!userService.isUsernameUnique(usr.getUsername())){
            throw new BadRequestException("Username "+usr.getUsername()+" already exists!");
        }
        if(!userService.isEmailUnique(usr.getEmail())){
            throw new BadRequestException("Email "+usr.getEmail()+" already exists!");
        }
        User user = null;
        try{
            usr.setRole(role);
            user = userService.saveUser(usr);
        }
        catch(Exception e){
            throw new InternalException("Cannot save this user to the database");
        }
        UserDto savedUsr = MapperUtils.mapperObject(user,UserDto.class);
        return ResponseEntity.ok(savedUsr);
    }
}
