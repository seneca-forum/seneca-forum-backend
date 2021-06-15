package com.seneca.senecaforum.service.dto;


import com.seneca.senecaforum.domain.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchDto {
    private Integer postId;
    private String title;
    private String content;
    private Topic topic;
    private List<Integer>idxKeywords;
}
