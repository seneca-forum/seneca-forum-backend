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
        if(userId.getClass()==obj.getClass()){
            return true;
        }
        Post post = (Post) obj;
        if(this.userId==null&&post.getPostId()!=null){
            return false;
        }
        else if(this.userId!=null && !this.userId.equals(post.getPostId())){
            return false;
        }
        return true;
    }
}
