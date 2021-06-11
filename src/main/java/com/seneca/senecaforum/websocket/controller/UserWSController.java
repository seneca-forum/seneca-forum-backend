package com.seneca.senecaforum.websocket.controller;

import com.seneca.senecaforum.client.exception.InternalException;
import com.seneca.senecaforum.websocket.config.WebSocketEventListener;
import com.seneca.senecaforum.websocket.service.MessageService;
import com.seneca.senecaforum.websocket.service.dto.OnlineUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/ws/users")
@CrossOrigin(origins = "*")
public class UserWSController {
    @Autowired
    WebSocketEventListener webSocketEventListener;

    @Autowired
    private MessageService messageService;


    @GetMapping("/{currentUserId}")
    public ResponseEntity<Set<OnlineUserDto>> getOnlineUsers(@PathVariable String currentUserId) {
        Set<OnlineUserDto>onls = new HashSet<>();
        try{
            onls = webSocketEventListener.getOnlineUsrs();
            if(onls!=null){
                onls.forEach(o->{
                    int count = messageService.countNewMessagesFromOnlineUser(currentUserId, o.getUserId());
                    o.setNoOfNewMessages(count);
                });
            }
        }
        catch(Exception ex){
            throw new InternalException("Cannot get the number of online users");
        }
        return new ResponseEntity<>(onls, HttpStatus.OK);
    }

}
