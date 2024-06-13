package com.raf.rafvodic.repositories.comments;


import com.raf.rafvodic.entities.Comment;

import java.util.List;

public interface CommentRepository {

    public Comment addComment(Comment comment);
    public List<Comment> getCommentsByArticleId(int id);
    public void deleteCommentsByArticleId(int id);
}
