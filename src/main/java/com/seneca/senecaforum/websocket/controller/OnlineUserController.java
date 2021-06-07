package com.seneca.senecaforum.websocket.controller;

import com.seneca.senecaforum.service.dto.PostViewDto;
import com.seneca.senecaforum.websocket.config.WebSocketEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class OnlineUserController {
    @Autowired
    WebSocketEventListener webSocketEventListener;

    @GetMapping("/onlines")
    public ResponseEntity<Set<Map<String, String>>> getOnlineUsers() {
        return new ResponseEntity<>(webSocketEventListener.getOnlineUsrs(), HttpStatus.OK);
    }
}
