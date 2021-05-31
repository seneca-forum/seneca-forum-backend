package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Role findByRoleName(String roleName);

    @Query(value = "select distinct r.role_id, r.code, r.role_name from users u " +
            "left outer join roles r on r.role_id = u.role_id where u.email =:email",
    nativeQuery = true)
    public Role findRoleNameByEmail(String email);
}
