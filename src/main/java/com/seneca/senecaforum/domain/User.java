package com.seneca.senecaforum.domain;

import com.seneca.senecaforum.service.utils.UserIdPrefixed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "user_id",updatable = false,nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_seq")
    @GenericGenerator(
            name = "user_seq",
            strategy = "com.seneca.senecaforum.service.utils.UserIdPrefixed",
            parameters = {
                    @Parameter(name = UserIdPrefixed.INCREMENT_PARAM,value = "1"),
                    @Parameter(name = UserIdPrefixed.CODE_NUMBER_SEPARATOR_PARAMETER,value = "_"),
                    @Parameter(name = UserIdPrefixed.NUMBER_FORMAT_PARAMETER,value = "%05d")
            }
    )
    private String userId;

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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="users_roles",
            joinColumns = @JoinColumn(name ="user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

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
