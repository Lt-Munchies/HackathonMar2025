package com.devconnect.services;

import com.devconnect.exceptions.ResourceNotFoundException;
import com.devconnect.models.Comment;
import com.devconnect.repositories.InMemoryCommentRepository;
import com.devconnect.repositories.InMemoryPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final InMemoryCommentRepository commentRepository;
    private final InMemoryPostRepository postRepository;

    public Comment createComment(Comment comment) {
        var post = postRepository.findById(comment.getPostId());
        if (post == null) {
            throw new ResourceNotFoundException("Post not found");
        }
        return commentRepository.save(comment);
    }

    public Comment getComment(UUID id) {
        Comment comment = commentRepository.findById(id);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment not found");
        }
        return comment;
    }

    public List<Comment> getCommentsByPostId(UUID postId) {
        var post = postRepository.findById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post not found");
        }
        return commentRepository.findByPostId(postId);
    }

    public Comment updateComment(UUID id, Comment updatedComment) {
        Comment existingComment = getComment(id);
        updatedComment.setId(id);
        updatedComment.setPostId(existingComment.getPostId());
        return commentRepository.save(updatedComment);
    }

    public void deleteComment(UUID id) {
        getComment(id); // Verify comment exists
        commentRepository.deleteById(id);
    }

    public void deleteAllByPostId(UUID postId) {
        commentRepository.deleteAllByPostId(postId);
    }
}
