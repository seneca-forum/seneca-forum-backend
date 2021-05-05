package com.seneca.senecaforum.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "email",length = 50,unique = true)
    private String email;

    @Column(name = "discord",length = 50)
    private String discord;

    @Column(name = "username",length = 50,unique = true)
    private String username;

    @Column(name = "password",length = 68,nullable = false)
    private String password;

    @Column(name="is_remember_me")
    private Boolean isRememberMe;

    @Column(name="created_on",nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createdOn;

}
