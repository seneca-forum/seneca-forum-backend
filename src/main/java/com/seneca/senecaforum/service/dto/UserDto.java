package com.seneca.senecaforum.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;

    private String email;

    private String username;

    private Date createdOn;

}
