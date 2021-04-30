package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.utils.NumberStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateNewUser(){
        String username = NumberStringUtils.generateRandomString(8,false,false,true,false);
        String password = NumberStringUtils.generateRandomString(12,true,true,true,false);
        User user = new User().builder()
                .username(username)
                .password(password)
                .createdOn(new Date())
                .build();
        userRepository.save(user);
    }

}
