package com.seneca.senecaforum.service;

import com.seneca.senecaforum.domain.Role;
import com.seneca.senecaforum.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role getRoleByRoleName(String rolename){
        return roleRepository.findByRoleName(rolename);
    }
}
