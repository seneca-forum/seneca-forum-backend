package com.seneca.senecaforum.websocket.controller;

import com.seneca.senecaforum.websocket.config.WebSocketEventListener;
import com.seneca.senecaforum.websocket.domain.ChatMessage;
import com.seneca.senecaforum.websocket.domain.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin("*")
public class ChatController {
    @Autowired
    WebSocketEventListener auth;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    @MessageMapping("/chat/{username}")
    public void sendMessage(@DestinationVariable String username
            , ChatMessage chatMessage
            , SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        chatMessage.setMessageType(MessageType.CHAT);
        headerAccessor.setSessionId(sessionId);
        messagingTemplate.convertAndSend("/topic/messages/"+username, chatMessage,headerAccessor.toMap());
    }


}
