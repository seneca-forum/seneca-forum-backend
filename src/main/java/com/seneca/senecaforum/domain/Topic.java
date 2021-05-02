package com.seneca.senecaforum.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "topics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic implements Comparable<Topic>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Integer topicId;

    @Column(name = "topic_name",nullable = false,unique = true)
    private String topicName;

    @Column(name = "views",nullable = false)
    private Integer views;

    @Override
    public int compareTo(Topic o) {
        int compareViews = o.getViews().compareTo(this.getViews());
        return compareViews;
    }

}
