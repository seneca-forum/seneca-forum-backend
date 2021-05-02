package com.seneca.senecaforum.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements EntityDto{
    private Integer userId;

    private String email;

    private String discord;

    private String username;

    private Date createdOn;
}
