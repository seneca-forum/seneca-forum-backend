package com.seneca.senecaforum.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "chatroom")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "chatroom_id")
    private String chatroomId;

    @Column(name="sender_id")
    private String senderId;

    @Column(name="recipient_id")
    private String recipientId;
}
