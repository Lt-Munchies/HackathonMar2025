package com.devconnect.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CodeBlock {
    @NotBlank
    private String content;
    
    @NotBlank
    private String language;
    
    private int position;

    private List<FoldingRange> foldingRanges = new ArrayList<>();
    
    @Data
    public static class FoldingRange {
        private int startLine;
        private int endLine;
        private String kind; // region, imports, comment, codeblock
        private String label; // Optional description for the folded region
    }

    public enum SyntaxTheme {
        GITHUB_DARK("github-dark"),
        GITHUB_LIGHT("github-light"),
        DRACULA("dracula"),
        MONOKAI("monokai");

        private final String value;

        SyntaxTheme(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static final List<String> SUPPORTED_LANGUAGES = List.of(
        "java", "kotlin", "python", "javascript", "typescript",
        "html", "css", "json", "xml", "markdown", "sql"
    );
}
