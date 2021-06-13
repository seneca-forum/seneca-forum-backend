package com.seneca.senecaforum.websocket.controller;

import com.seneca.senecaforum.client.exception.InternalException;
import com.seneca.senecaforum.domain.UserEntity;
import com.seneca.senecaforum.service.UserService;
import com.seneca.senecaforum.service.utils.MapperUtils;
import com.seneca.senecaforum.websocket.config.WebSocketEventListener;
import com.seneca.senecaforum.websocket.service.MessageService;
import com.seneca.senecaforum.websocket.service.dto.OnlineUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ws/users")
@CrossOrigin(origins = "*")
public class UserWSController {
    @Autowired
    WebSocketEventListener webSocketEventListener;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;


    @GetMapping("/{currentUserId}")
    public ResponseEntity<List<OnlineUserDto>> getOnlineUsers(@PathVariable String currentUserId) {
        List<OnlineUserDto>usersWithStatus = new ArrayList<>();
        List<OnlineUserDto>list = null;

        List<OnlineUserDto>offlineUsers = MapperUtils.mapperList(userService.getAllUsers(),OnlineUserDto.class);
        offlineUsers.stream().map(u->{
            u.setStatus("OFFLINE");
            return u;
        }).collect(Collectors.toList());

        try{
            List<OnlineUserDto>onls = webSocketEventListener.getOnlineUsrs().stream().collect(Collectors.toList());
            if(onls!=null){
                onls.forEach(o->{
                    int count = messageService.countNewMessagesFromOnlineUser(currentUserId, o.getUserId());
                    o.setNoOfNewMessages(count);
                    o.setStatus("ONLINE");
                });
                usersWithStatus.addAll(onls);
            }
            offlineUsers.forEach(u->{
                if(onls.stream().map(OnlineUserDto::getUsername).collect(Collectors.toList()).contains(u.getUsername())==false){
                    usersWithStatus.add(u);
                }
            });
        }
        catch(Exception ex){
            throw new InternalException("Cannot get the number of online users");
        }
        return new ResponseEntity<>(usersWithStatus, HttpStatus.OK);
    }
}

