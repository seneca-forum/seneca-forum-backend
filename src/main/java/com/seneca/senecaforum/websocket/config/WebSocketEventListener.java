package com.seneca.senecaforum.websocket.config;

import com.seneca.senecaforum.websocket.domain.ChatMessage;
import com.seneca.senecaforum.websocket.domain.MessageType;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
public class WebSocketEventListener {
    private Set<Map<String, String>> onlineUsrs = new HashSet<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);


    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        @SuppressWarnings("rawtypes")
        GenericMessage connectHeader = (GenericMessage) stompAccessor
                .getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER); // FIXME find a way to pass the username
        // to the server
        @SuppressWarnings("unchecked")
        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) connectHeader.getHeaders()
                .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

        String login = nativeHeaders.get("username").get(0);
        String sessionId = stompAccessor.getSessionId();
        System.out.println("Chat connection by user "+login+" with sessionId " +sessionId);
        ChatMessage chatMessage = ChatMessage
                .builder().message(sessionId)
                .messageType(MessageType.JOIN)
                .fromLogin(login)
                .build();
        if(this.onlineUsrs==null){
            this.onlineUsrs = new HashSet<>();
        }
        this.onlineUsrs.add(Map.of(login,sessionId));
        messagingTemplate.convertAndSend("/topic/chat", chatMessage,stompAccessor.toMap());

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = stompAccessor.getSessionId();
        System.out.println("Chat disconnection with sessionId " +sessionId+" login ");
        ChatMessage chatMessage = ChatMessage.builder().messageType(MessageType.LEAVE).message(sessionId).build();
        Map<String, String>offlineUsr = this.onlineUsrs
                .stream()
                .filter((a)->a.containsValue(sessionId))
                .collect(Collectors.toList()).get(0);
        this.onlineUsrs.remove(offlineUsr);
        messagingTemplate.convertAndSend("/topic/chat", chatMessage,stompAccessor.toMap());
    }

}
