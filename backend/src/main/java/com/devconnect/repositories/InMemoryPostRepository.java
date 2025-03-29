package com.devconnect.repositories;

import com.devconnect.models.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPostRepository {
    private final Map<UUID, Post> posts = new ConcurrentHashMap<>();

    public Post save(Post post) {
        posts.put(post.getId(), post);
        return post;
    }

    public Post findById(UUID id) {
        return posts.get(id);
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

    public void deleteById(UUID id) {
        posts.remove(id);
    }

    public List<Post> search(String query, String language, Boolean hasAiComments) {
        return posts.values().stream()
            .filter(post -> matchesQuery(post, query))
            .filter(post -> matchesLanguage(post, language))
            .filter(post -> matchesAiComments(post, hasAiComments))
            .toList();
    }

    private boolean matchesQuery(Post post, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }
        String lowerQuery = query.toLowerCase();
        return post.getTitle().toLowerCase().contains(lowerQuery) ||
               (post.getContent() != null && post.getContent().toLowerCase().contains(lowerQuery));
    }

    private boolean matchesLanguage(Post post, String language) {
        if (language == null || language.isBlank()) {
            return true;
        }
        return post.getCodeBlocks().stream()
            .anyMatch(block -> block.getLanguage().equalsIgnoreCase(language));
    }

    private boolean matchesAiComments(Post post, Boolean hasAiComments) {
        return hasAiComments == null || post.getCommentCount() > 0;
    }
}
