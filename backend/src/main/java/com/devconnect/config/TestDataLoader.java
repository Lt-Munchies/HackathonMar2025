package com.devconnect.config;

import com.devconnect.models.Post;
import com.devconnect.models.CodeBlock;
import com.devconnect.repositories.InMemoryPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {
    private final InMemoryPostRepository postRepository;

    @Override
    public void run(String... args) {
        // Create test post
        Post post = new Post();
        post.setTitle("Welcome to DevConnect!");
        post.setContent("This is a test post to demonstrate the functionality.");
        post.setAuthorId(UUID.randomUUID());
        post.setAuthorAlias("TestUser");

        CodeBlock codeBlock = new CodeBlock();
        codeBlock.setContent("console.log('Hello DevConnect!');");
        codeBlock.setLanguage("javascript");
        codeBlock.setPosition(0);
        post.getCodeBlocks().add(codeBlock);

        postRepository.save(post);
    }
}
