package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Post;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.utils.NumberStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateNewUser(){
        int before = userRepository.findAll().size();
        String username = NumberStringUtils.generateRandomString(8,false,false,true,false);
        String password = NumberStringUtils.generateRandomString(12,true,true,true,false);
        User user = new User().builder()
                .username(username)
                .password(password)
                .createdOn(new Date())
                .isRememberMe(false)
                .build();
        userRepository.save(user);
        int after = userRepository.findAll().size();
        assertThat(before).isEqualTo(after-1);
    }

}
