package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateUserAdminRoles(){
        Role admin = new Role().builder()
                .roleName("ROLE_ADMIN")
                .code("AD")
                .build();

        Role user = new Role().builder()
                .roleName("ROLE_USER")
                .code("U")
                .build();

        roleRepository.saveAll(List.of(admin,user));
    }

    @Test
    public void testGetRoleByEmail(){
        Role role = roleRepository.findRoleNameByEmail("trang@gmail.com");
        assertThat(role.getRoleName()).isEqualTo("ROLE_USER");
    }


}
