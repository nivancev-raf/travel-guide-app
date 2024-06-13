package com.raf.rafvodic.services;

import com.raf.rafvodic.repositories.comments.CommentRepository;
import com.raf.rafvodic.entities.Comment;

import javax.inject.Inject;

import java.util.List;

public class CommentService {

    @Inject
    CommentRepository commentRepository;

    public CommentService() {
    }

    public Comment addComment(Comment comment) {
        return commentRepository.addComment(comment);
    }

    public List<Comment> getCommentsByArticleId(int id) {
        return commentRepository.getCommentsByArticleId(id);
    }

    public void deleteCommentsByArticleId(int id) {
        commentRepository.deleteCommentsByArticleId(id);
    }

}
