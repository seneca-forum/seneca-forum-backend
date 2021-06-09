package com.seneca.senecaforum.websocket.controller;

import com.seneca.senecaforum.service.dto.PostViewDto;
import com.seneca.senecaforum.websocket.config.WebSocketEventListener;
import com.seneca.senecaforum.websocket.domain.Chatroom;
import com.seneca.senecaforum.websocket.domain.Message;
import com.seneca.senecaforum.websocket.repository.ChatroomRepository;
import com.seneca.senecaforum.websocket.repository.MessageRepository;
import com.seneca.senecaforum.websocket.service.dto.OnlineUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class OnlineUserController {
    @Autowired
    WebSocketEventListener webSocketEventListener;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatroomRepository chatroomRepository;

    @GetMapping("/onlines/{currentUserId}")
    public ResponseEntity<Set<OnlineUserDto>> getOnlineUsers(@PathVariable String currentUserId) {
        Set<OnlineUserDto>onls = webSocketEventListener.getOnlineUsrs();

        onls.forEach(o->{
            int count = messageRepository.countNewMessagesFromOnlineUser(currentUserId, o.getUserId());
            o.setNoOfNewMessages(count);
        });

        return new ResponseEntity<>(onls, HttpStatus.OK);
    }

    @GetMapping("/{senderId}/{recipientId}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable String senderId,
                                                              @PathVariable String recipientId) {
        List<Message> msgs = messageRepository.findChatMessagesFromSelectedUser(senderId, recipientId);
        msgs.stream().filter(m->m.getStatus().equals("RECEIVED")).forEach(m->{
            m.setStatus("DELIVERED");
            messageRepository.save(m);
        });
        Optional<Chatroom> cr = chatroomRepository.findChatroomBySenderIdAndRecipientId(senderId, recipientId);
        List<Message>allMes = null;
        if(cr.isPresent()){
            allMes = messageRepository.findChatMessagesByChatroomId(cr.get().getChatroomId());
        }
        return new ResponseEntity<List<Message>>(allMes, HttpStatus.OK);
    }
}
