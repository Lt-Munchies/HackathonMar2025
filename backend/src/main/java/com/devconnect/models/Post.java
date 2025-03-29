package com.devconnect.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class Post {
    private UUID id = UUID.randomUUID();
    
    @NotBlank
    @Size(min = 1, max = 255)
    private String title;
    
    private String content;
    
    private List<CodeBlock> codeBlocks = new ArrayList<>();
    
    @NotBlank
    private UUID authorId;
    
    @NotBlank
    @Size(min = 3, max = 30)
    private String authorAlias;
    
    private Instant createdAt = Instant.now();
    
    private int commentCount;
    
    private Map<ReactionType, Integer> reactions = new HashMap<>();
    
    public enum ReactionType {
        HEART, THUMBS_UP, ROCKET
    }
}
