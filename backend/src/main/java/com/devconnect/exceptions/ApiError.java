package com.devconnect.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
public class ApiError {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;
    private int status;
    private String code;
    private String message;
    private Map<String, Object> details;

    public static ApiError of(String code, String message, int status) {
        return ApiError.builder()
                .code(code)
                .message(message)
                .status(status)
                .timestamp(Instant.now())
                .build();
    }

    public static ApiError of(String code, String message, int status, Map<String, Object> details) {
        return ApiError.builder()
                .code(code)
                .message(message)
                .status(status)
                .details(details)
                .timestamp(Instant.now())
                .build();
    }
}
