package com.seneca.senecaforum.websocket.controller;

import com.seneca.senecaforum.repository.UserRepository;
import com.seneca.senecaforum.service.utils.MapperUtils;
import com.seneca.senecaforum.websocket.config.WebSocketEventListener;
import com.seneca.senecaforum.websocket.domain.Chatroom;
import com.seneca.senecaforum.websocket.domain.Message;
import com.seneca.senecaforum.websocket.repository.ChatroomRepository;
import com.seneca.senecaforum.websocket.repository.MessageRepository;
import com.seneca.senecaforum.websocket.service.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Controller
@CrossOrigin("*")
public class ChatController {
    @Autowired
    WebSocketEventListener auth;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatroomRepository chatroomRepository;

    @MessageMapping("/chat")
    public void sendMessage(Message chatMessage
            , SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        headerAccessor.setSessionId(sessionId);

        Optional<Chatroom> chatroom = chatroomRepository.findChatroomBySenderIdAndRecipientId(chatMessage.getSenderId(),chatMessage.getRecipientId());
        String chatroomId = "";
        if(chatroom.isEmpty()){
             chatroomId= String.format("%s_%s", chatMessage.getSenderId(), chatMessage.getRecipientId());

            Chatroom senderRecipient = Chatroom
                    .builder()
                    .chatroomId(chatroomId)
                    .senderId(chatMessage.getSenderId())
                    .recipientId(chatMessage.getRecipientId())
                    .build();

            Chatroom recipientSender = Chatroom
                    .builder()
                    .chatroomId(chatroomId)
                    .senderId(chatMessage.getRecipientId())
                    .recipientId(chatMessage.getSenderId())
                    .build();
            chatroomRepository.save(senderRecipient);
            chatroomRepository.save(recipientSender);
        }
        else{
            chatroomId = chatroom.get().getChatroomId();
        }
        chatMessage.setChatroomId(chatroomId);
        Message saved = messageRepository.save(chatMessage);

        NotificationDto noti = MapperUtils.mapperObject(saved, NotificationDto.class);

        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientName(),"/queue/messages",noti);
    }


}
