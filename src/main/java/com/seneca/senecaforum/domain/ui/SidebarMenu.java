package com.seneca.senecaforum.domain.ui;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

import org.hibernate.annotations.OrderBy;

@Entity
@Data
@Table(name = "sidebar_menu")
public class SidebarMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sidebar_id")
    private int id;
    private String name;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "sidebar_id")
    private Set<Topic> topics;

}
