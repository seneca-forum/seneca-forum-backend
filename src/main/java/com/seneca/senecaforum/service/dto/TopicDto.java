package com.seneca.senecaforum.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    private String topicId;

    private String topicName;

    private Integer views;

    private Integer noOfPosts;
}
