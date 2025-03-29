package com.devconnect.models.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class PostSearchRequest {
    private String query;
    private String language;
    private Boolean hasAiComments;
    private Instant fromDate;
    private Instant toDate;
    private int page = 0;
}
