package com.seneca.senecaforum.websocket.service;

import com.seneca.senecaforum.websocket.domain.Chatroom;
import com.seneca.senecaforum.websocket.domain.Message;
import com.seneca.senecaforum.websocket.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public int countNewMessagesFromOnlineUser(String currentUserId, String userId){
        return messageRepository.countNewMessagesFromOnlineUser(currentUserId, userId);
    }

    public List<Message>findChatMessagesFromSelectedUser(String senderId, String recipientId){
        return messageRepository.findChatMessagesFromSelectedUser(senderId, recipientId);
    }

    public void updateMessagesStatusToDelivered(List<Message>msgs){
        msgs.stream().filter(m->m.getStatus().equals("RECEIVED")).forEach(m->{
            m.setStatus("DELIVERED");
            messageRepository.save(m);
        });
    }

    public List<Message> findChatMessagesByChatroomId(String chatroomId){
        return messageRepository.findChatMessagesByChatroomId(chatroomId);
    }
}
