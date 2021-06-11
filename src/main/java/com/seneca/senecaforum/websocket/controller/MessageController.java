package com.seneca.senecaforum.websocket.controller;

import com.seneca.senecaforum.client.exception.InternalException;
import com.seneca.senecaforum.websocket.domain.Chatroom;
import com.seneca.senecaforum.websocket.domain.Message;
import com.seneca.senecaforum.websocket.service.ChatroomService;
import com.seneca.senecaforum.websocket.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ws/messages")
@CrossOrigin(origins = "*")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatroomService chatroomService;


    @GetMapping("/{senderId}/{recipientId}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable String senderId,
                                                         @PathVariable String recipientId) {
        List<Message>messagesFromSenderRepicient = null;
        try{
            List<Message> msgs = messageService.findChatMessagesFromSelectedUser(senderId, recipientId);
            messageService.updateMessagesStatusToDelivered(msgs);

            Chatroom cr = chatroomService.findChatroomBySenderIdAndRecipientId(senderId, recipientId);

            if(cr!=null){
                messagesFromSenderRepicient = messageService.findChatMessagesByChatroomId(cr.getChatroomId());
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new InternalException("Cannot find messages between sender "+senderId+" and recipient "+recipientId);
        }
        return new ResponseEntity<List<Message>>(messagesFromSenderRepicient, HttpStatus.OK);
    }

}
