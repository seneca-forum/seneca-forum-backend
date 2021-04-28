package com.seneca.senecaforum.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;

@Entity
@Table(name = "topic")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private int topicId;

    @Column(name = "topic_name")
    private String topicName;

    @Column(name = "views")
    @OrderBy(clause = "desc")
    private int views;

}
