package com.devconnect.models.dto;

import lombok.Data;

@Data
public class AiCommentRequest {
    private Integer codeBlockIndex;
    private String specificQuestion;
}
