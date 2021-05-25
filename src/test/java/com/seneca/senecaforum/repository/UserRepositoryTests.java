package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Role;
import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.utils.DatabaseUtils;
import com.seneca.senecaforum.utils.NumberStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateNewUser(){
        int before = userRepository.findAll().size();
        String email = NumberStringUtils.generateRandomString(5,false,false,true,false)+"@gmail.com";
        String username = NumberStringUtils.generateRandomString(8,false,false,true,false);
        String password = NumberStringUtils.generateRandomString(12,true,true,true,false);
        User user = new User().builder()
                .email(email)
                .username(username)
                .password(password)
                .createdOn(new Date())
                .role(roleRepository.findByRoleName("ROLE_USER"))
                .isRememberMe(false)
                .build();
        userRepository.save(user);
        int after = userRepository.findAll().size();
        assertThat(before).isEqualTo(after-1);
    }


    @Test
    public void testDeleteAUser(){
        User randomUsr = DatabaseUtils.generateRandomObjFromUserDb(userRepository);
        userRepository.delete(randomUsr);
        Optional<User> deletedUsr = userRepository.findById(randomUsr.getUserId());
        assertThat(deletedUsr).isEmpty();
    }

}
