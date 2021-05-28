package com.seneca.senecaforum.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicStatDto {
    private Integer topicId;

    private String topicName;

    private Integer views;

    private Integer noOfPosts;

    private Integer noOfComments;
}
