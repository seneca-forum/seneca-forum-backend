package com.seneca.senecaforum.service.dto;


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
    private List<Integer>idxKeywords;
}
