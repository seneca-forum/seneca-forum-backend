package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Role findByRoleName(String roleName);
}
