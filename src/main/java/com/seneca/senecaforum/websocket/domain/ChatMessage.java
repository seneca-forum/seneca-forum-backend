package com.seneca.senecaforum.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String fromLogin;
    private String message;
    private MessageType messageType;
}
