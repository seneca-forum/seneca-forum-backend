package com.seneca.senecaforum.repository;

import com.seneca.senecaforum.domain.SidebarMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SidebarRepository extends JpaRepository<SidebarMenu,Integer> {

}