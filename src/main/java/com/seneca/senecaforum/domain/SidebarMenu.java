package com.seneca.senecaforum.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Map;
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "topics",joinColumns = @JoinColumn(name = "sidebar_id"))
    @MapKeyColumn(name = "topic_name")
    @Column(name = "views")
    @OrderBy(clause = "views desc")
    private Map<String,Integer> topics;


}
