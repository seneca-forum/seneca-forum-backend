package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.BadRequestException;
import com.seneca.senecaforum.client.exception.InternalException;
import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.Role;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.service.PostService;
import com.seneca.senecaforum.service.RoleService;
import com.seneca.senecaforum.service.UserService;
import com.seneca.senecaforum.service.dto.PostViewDto;
import com.seneca.senecaforum.service.dto.UserDto;
import com.seneca.senecaforum.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PostService postService;

    @PostMapping("/new")
    public ResponseEntity<UserDto> createNewUser(@RequestBody User usr) {
        Role role = roleService.getRoleByRoleName(usr.getRole().getRoleName());
        if (role == null) {
            throw new BadRequestException("Rolename " + usr.getRole().getRoleName() + "does not exist!");
        }
        if (!userService.isUsernameUnique(usr.getUsername())) {
            throw new BadRequestException("Username " + usr.getUsername() + " already exists!");
        }
        if (!userService.isEmailUnique(usr.getEmail())) {
            throw new BadRequestException("Email " + usr.getEmail() + " already exists!");
        }
        User user = null;
        try {
            usr.setRole(role);
            user = userService.saveUser(usr);
        } catch (Exception e) {
            throw new InternalException("Cannot save this user to the database");
        }
        UserDto savedUsr = MapperUtils.mapperObject(user, UserDto.class);
        return ResponseEntity.ok(savedUsr);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody User usr) {
        Authentication authentication = null;
        if(userService.isEmailUnique(usr.getEmail())){
           throw new BadRequestException("Email "+usr.getEmail()+" is not found!");
        }
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usr.getEmail(), usr.getPassword())
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("Email and Password is not matched!");
        }
        // update is rememberMe
        User savedUsr = userService.updateIsRememberMe(usr);
        String jwt = userService.createNewToken(authentication,usr.getIsRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);

        UserDto userDto = MapperUtils.mapperObject(savedUsr,UserDto.class);
        return new ResponseEntity<>(userDto, httpHeaders, HttpStatus.OK);

    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<PostViewDto>> getAllPostsByUserId(@PathVariable("userId")  String userId) {
        if(!userService.isUserIdValid(userId)){
            throw new BadRequestException("Cannot find any users with userId "+userId);
        }
        List<Post>posts = postService.getAllPostsByUserId(userId);
        List<PostViewDto>postDtos = MapperUtils.mapperList(posts,PostViewDto.class);
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable("userId")  String userId) {
        UserDto usr = null;
        try{
            User user = userService.getUserByUserId(userId);
            usr = MapperUtils.mapperObject(user,UserDto.class);
        }
        catch (Exception ex){
            throw new InternalException("Cannot find any users with userId "+ userId);
        }
        return new ResponseEntity<>(usr, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public ResponseEntity getUserByUserId(
            @PathVariable("userId")  String userId,
            @PathVariable("postId")  int postId) {
        UserDto usr = null;
        if(!userService.isUserIdValid(userId)){
            throw new BadRequestException("Cannot find any users with userId "+userId);
        }
        if(!postService.isPostIdValid(postId)){
            throw new BadRequestException("Cannot find any post with postId "+postId);
        }
        try{
            postService.deleteAPost(postId);
        }
        catch(Exception ex){
            throw new InternalException("Cannot delete this post with postId "+postId+" !");
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}