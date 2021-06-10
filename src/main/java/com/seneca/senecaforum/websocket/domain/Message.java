package com.seneca.senecaforum.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private int messageId;

    @Column(name="chatroom_id")
    private String chatroomId;

    @Column(name="sender_id")
    private String senderId;

    @Column(name="recipient_id")
    private String recipientId;

    @Column(name="sender_name")
    private String senderName;

    @Column(name="recipient_name")
    private String recipientName;

    @Column
    private String content;

    @Column(name = "created_on",nullable = false)
    private Date createdOn;

    private String status;
}
