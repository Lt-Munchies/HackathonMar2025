package com.devconnect.controllers;

import com.devconnect.models.Post;
import com.devconnect.models.dto.PostSearchRequest;
import com.devconnect.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(
        @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(postService.getPosts(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable UUID id) {
        Post post = postService.getPost(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
        @PathVariable UUID id,
        @Valid @RequestBody Post post
    ) {
        return ResponseEntity.ok(postService.updatePost(id, post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reactions/{type}")
    public ResponseEntity<Post> addReaction(
        @PathVariable UUID id,
        @PathVariable Post.ReactionType type
    ) {
        return ResponseEntity.ok(postService.addReaction(id, type));
    }

    @DeleteMapping("/{id}/reactions/{type}")
    public ResponseEntity<Post> removeReaction(
        @PathVariable UUID id,
        @PathVariable Post.ReactionType type
    ) {
        return ResponseEntity.ok(postService.removeReaction(id, type));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> search(@Valid PostSearchRequest request) {
        return ResponseEntity.ok(postService.search(request));
    }
}
