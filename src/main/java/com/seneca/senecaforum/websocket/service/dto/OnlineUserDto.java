package com.seneca.senecaforum.websocket.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUserDto {
    private String userId;
    private String sessionId;
    private String username;
    private Integer noOfNewMessages;
}
