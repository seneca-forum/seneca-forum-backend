package com.seneca.senecaforum.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "topics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic{
    @Id
    @Column(name = "topic_id",updatable = false,nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "topic_gen")
    @GenericGenerator(
            name = "topic_gen",
            strategy = "com.seneca.senecaforum.service.utils.TopicIdPrefixed",
            parameters = {}
    )
    private String topicId;

    @Column(name = "topic_name",nullable = false,unique = true)
    private String topicName;



}
