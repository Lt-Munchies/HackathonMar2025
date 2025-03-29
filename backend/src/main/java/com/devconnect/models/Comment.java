package com.devconnect.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Comment {
    private UUID id = UUID.randomUUID();
    
    @NotBlank
    private String content;
    
    private List<CodeBlock> codeBlocks = new ArrayList<>();
    
    @NotBlank
    private UUID authorId;
    
    @NotBlank
    @Size(min = 3, max = 30)
    private String authorAlias;
    
    private boolean isAiGenerated;
    
    private UUID postId;
    
    private Instant createdAt = Instant.now();
}
