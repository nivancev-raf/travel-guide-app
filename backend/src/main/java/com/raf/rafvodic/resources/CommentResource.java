package com.raf.rafvodic.resources;


import com.raf.rafvodic.services.CommentService;
import com.raf.rafvodic.entities.Comment;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/comments")
public class CommentResource {

    @Inject
    private CommentService commentService;

    public CommentResource() {
    }

    // add comment
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Comment create(@Valid Comment comment) {
        return this.commentService.addComment(comment);
    }

    // get comments by article id
    @GET
    @Path("/article/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getCommentsByArticleId(@PathParam("id")int id) {
        return this.commentService.getCommentsByArticleId(id);
    }

    // delete comments by article id
    @DELETE
    @Path("/article/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteCommentsByArticleId(@PathParam("id")int id) {
        this.commentService.deleteCommentsByArticleId(id);
    }


}
