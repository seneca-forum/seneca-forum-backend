package com.seneca.senecaforum.websocket.service;

import com.seneca.senecaforum.websocket.domain.Chatroom;
import com.seneca.senecaforum.websocket.repository.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatroomService {
    @Autowired
    ChatroomRepository chatroomRepository;


    public Chatroom findChatroomBySenderIdAndRecipientId(String senderId, String recipientId){
        Optional<Chatroom> found = chatroomRepository.findChatroomBySenderIdAndRecipientId(senderId, recipientId);
        if(found.isPresent()){
            return found.get();
        }
        return null;
    }
}
