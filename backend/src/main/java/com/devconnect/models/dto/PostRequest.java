package com.devconnect.models.dto;

import com.devconnect.models.CodeBlock;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PostRequest {
    @NotBlank
    @Size(min = 1, max = 255)
    private String title;
    
    private String content;
    
    private List<CodeBlock> codeBlocks;
    
    @NotBlank
    private UUID authorId;
    
    @NotBlank
    @Size(min = 3, max = 30)
    private String authorAlias;
}
