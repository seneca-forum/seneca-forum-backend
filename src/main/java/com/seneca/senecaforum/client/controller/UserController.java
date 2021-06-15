package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.client.exception.BadRequestException;
import com.seneca.senecaforum.client.exception.InternalException;
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

    @Autowired
    private PostService postService;

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
                new URI(ApplicationConstants.BASE_URL+"/posts/"+usr.getUserId()))
                .body(userService.saveUser(usr));
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
            UserEntity userEntity = userService.getUserByUserId(userId);
            usr = MapperUtils.mapperObject(userEntity,UserDto.class);
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
