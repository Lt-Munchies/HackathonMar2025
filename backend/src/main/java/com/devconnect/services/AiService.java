package com.devconnect.services;

import com.devconnect.exceptions.AiServiceException;
import com.devconnect.models.CodeBlock;
import com.devconnect.models.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AiService {

    public String generateResponse(Post post, Integer codeBlockIndex, String specificQuestion) {
        try {
            String prompt = buildPrompt(post, codeBlockIndex, specificQuestion);
            log.debug("Sending prompt to AI service: {}", prompt);

            // For now, return a mock response
            return "This is a mock AI response for the post '" + post.getTitle() + "'. " +
                   "In a real implementation, this would call the Azure OpenAI API with the following prompt:\n\n" + prompt;

        } catch (Exception e) {
            log.error("Error generating AI response", e);
            throw new AiServiceException("Failed to generate AI response", e);
        }
    }

    private String buildPrompt(Post post, Integer codeBlockIndex, String specificQuestion) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Post Title: ").append(post.getTitle()).append("\n\n");
        prompt.append("Post Content: ").append(post.getContent()).append("\n\n");

        List<CodeBlock> codeBlocks = post.getCodeBlocks();
        if (codeBlockIndex != null && codeBlockIndex >= 0 && codeBlockIndex < codeBlocks.size()) {
            CodeBlock targetBlock = codeBlocks.get(codeBlockIndex);
            prompt.append("Code Block (").append(targetBlock.getLanguage()).append("):\n");
            prompt.append(targetBlock.getContent()).append("\n\n");
        } else if (!codeBlocks.isEmpty()) {
            for (int i = 0; i < codeBlocks.size(); i++) {
                CodeBlock block = codeBlocks.get(i);
                prompt.append("Code Block ").append(i + 1).append(" (").append(block.getLanguage()).append("):\n");
                prompt.append(block.getContent()).append("\n\n");
            }
        }

        if (specificQuestion != null && !specificQuestion.isEmpty()) {
            prompt.append("Specific Question: ").append(specificQuestion).append("\n\n");
        }

        prompt.append("Please provide a helpful response addressing the post and any specific questions.");
        return prompt.toString();
    }
}
