package com.devconnect.repositories;

import com.devconnect.models.Comment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryCommentRepository {
    private final Map<UUID, Comment> comments = new ConcurrentHashMap<>();
    private final Map<UUID, List<UUID>> postComments = new ConcurrentHashMap<>();

    public Comment save(Comment comment) {
        comments.put(comment.getId(), comment);
        postComments.computeIfAbsent(comment.getPostId(), k -> new ArrayList<>())
            .add(comment.getId());
        return comment;
    }

    public Comment findById(UUID id) {
        return comments.get(id);
    }

    public List<Comment> findByPostId(UUID postId) {
        List<UUID> commentIds = postComments.getOrDefault(postId, Collections.emptyList());
        return commentIds.stream()
            .map(comments::get)
            .filter(Objects::nonNull)
            .toList();
    }

    public void deleteById(UUID id) {
        Comment comment = comments.remove(id);
        if (comment != null) {
            postComments.computeIfPresent(comment.getPostId(), (k, v) -> {
                v.remove(id);
                return v.isEmpty() ? null : v;
            });
        }
    }

    public void deleteAllByPostId(UUID postId) {
        List<UUID> commentIds = postComments.remove(postId);
        if (commentIds != null) {
            commentIds.forEach(comments::remove);
        }
    }
}
