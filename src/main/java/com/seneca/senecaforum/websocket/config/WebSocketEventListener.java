package com.seneca.senecaforum.websocket.config;


import com.seneca.senecaforum.domain.UserEntity;
import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.service.utils.MapperUtils;
import com.seneca.senecaforum.websocket.domain.Message;
import com.seneca.senecaforum.websocket.service.dto.OnlineUserDto;
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
    private Set<OnlineUserDto> onlineUsrs = new HashSet<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);


    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserRepository userRepository;

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
        if(this.onlineUsrs==null){
            this.onlineUsrs = new HashSet<>();
        }
        UserEntity usr = userRepository.findByUsername(login).get();
        OnlineUserDto onl = MapperUtils.mapperObject(usr, OnlineUserDto.class);
        onl.setSessionId(sessionId);
        this.onlineUsrs.add(onl);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = stompAccessor.getSessionId();
        OnlineUserDto offlineUsr = this.onlineUsrs
                .stream()
                .filter((a)->a.getSessionId().equals(sessionId))
                .collect(Collectors.toList()).get(0);
        this.onlineUsrs.remove(offlineUsr);
    }

}
