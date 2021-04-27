package com.seneca.senecaforum.client.controller;

import com.seneca.senecaforum.domain.SidebarMenu;
import com.seneca.senecaforum.repository.SidebarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SidebarController {

    @Autowired
    private SidebarRepository sidebarRepository;

    @GetMapping("/sidebar")
    public ResponseEntity<List<SidebarMenu>> getAllSidebar(){
        return ResponseEntity.ok(sidebarRepository.findAll());
    }
}
