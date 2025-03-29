package com.devconnect.controllers;

import com.devconnect.models.Comment;
import com.devconnect.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable UUID postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(
        @PathVariable UUID postId,
        @Valid @RequestBody Comment comment
    ) {
        comment.setPostId(postId);
        return ResponseEntity.ok(commentService.createComment(comment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(
        @PathVariable UUID postId,
        @PathVariable UUID id
    ) {
        Comment comment = commentService.getComment(id);
        if (!comment.getPostId().equals(postId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(
        @PathVariable UUID postId,
        @PathVariable UUID id,
        @Valid @RequestBody Comment comment
    ) {
        Comment existing = commentService.getComment(id);
        if (!existing.getPostId().equals(postId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commentService.updateComment(id, comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable UUID postId,
        @PathVariable UUID id
    ) {
        Comment comment = commentService.getComment(id);
        if (!comment.getPostId().equals(postId)) {
            return ResponseEntity.notFound().build();
        }
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}
