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

    @Override
    public int hashCode() {
        int prime = 31;
        return prime+ ((userId==null)?0:prime+userId.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return false;
        }
        OnlineUserDto user = (OnlineUserDto) obj;
        if(!this.userId.equals(((OnlineUserDto) obj).getUserId())){
            return false;
        }
        else if(!this.sessionId.equals(((OnlineUserDto) obj).sessionId)){
            return false;
        }
        return true;
    }
}
