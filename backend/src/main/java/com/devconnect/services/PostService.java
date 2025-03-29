package com.devconnect.services;

import com.devconnect.exceptions.ResourceNotFoundException;
import com.devconnect.models.Post;
import com.devconnect.models.dto.PostSearchRequest;
import com.devconnect.repositories.InMemoryPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final InMemoryPostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post getPost(UUID id) {
        return postRepository.findById(id);
    }

    public List<Post> getPosts(int page) {
        return postRepository.findAll().stream()
            .skip(page * 20L)
            .limit(20)
            .toList();
    }

    public Post updatePost(UUID id, Post post) {
        Post existingPost = postRepository.findById(id);
        if (existingPost == null) {
            throw new ResourceNotFoundException("Post not found");
        }
        post.setId(id);
        return postRepository.save(post);
    }

    public void deletePost(UUID id) {
        postRepository.deleteById(id);
    }

    public List<Post> search(PostSearchRequest request) {
        return postRepository.search(
            request.getQuery(),
            request.getLanguage(),
            request.getHasAiComments()
        ).stream()
            .skip(request.getPage() * 20L)
            .limit(20)
            .toList();
    }

    public Post addReaction(UUID id, Post.ReactionType type) {
        Post post = getPost(id);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found");
        }
        post.getReactions().merge(type, 1, Integer::sum);
        return postRepository.save(post);
    }

    public Post removeReaction(UUID id, Post.ReactionType type) {
        Post post = getPost(id);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found");
        }
        post.getReactions().merge(type, -1, (a, b) -> Math.max(0, a + b));
        return postRepository.save(post);
    }
}
