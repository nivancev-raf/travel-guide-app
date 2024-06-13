package com.raf.rafvodic.resources;

import com.raf.rafvodic.entities.Article;
import com.raf.rafvodic.entities.Destination;
import com.raf.rafvodic.services.ArticleService;
import com.raf.rafvodic.services.DestinationService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/articles")
public class ArticleResource {

    @Inject
    private ArticleService articleService;

    public ArticleResource() {
    }

    // @GET for all articles
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Article> allArticles(@QueryParam("page") @DefaultValue("1") int page,
                                     @QueryParam("limit") @DefaultValue("10") int limit) {
        return this.articleService.getAllArticles(page, limit);
    }

    // Get most read articles
    @GET
    @Path("/mostRead")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Article> mostRead(
            @QueryParam("limit") @DefaultValue("10") int limit,
            @QueryParam("sort") @DefaultValue("visitCount,desc") String sort,
            @QueryParam("filter") String filter) {
        return this.articleService.getMostReadArticles(limit, sort, filter);
    }

    // add article
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Article create(@Valid Article article) {
        return this.articleService.addArticle(article);
    }

    // get article by destinationId
    @GET
    @Path("/destination/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Article> getArticlesByDestinationId(@PathParam("id")int id) {
        return this.articleService.getArticlesByDestinationId(id);
    }

    // get article by id
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Article getArticleById(@PathParam("id")int id) {
        return this.articleService.findArticleById(id);
    }

    // update article by id
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Article updateArticle(@PathParam("id")int id, @Valid Article article) {
        return articleService.updateArticle(article);
    }

    // get articles by activity id
    @GET
    @Path("/activity/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Article> getArticlesByActivityId(@PathParam("id")int id) {
        return this.articleService.getArticlesByActivityId(id);
    }

    // delete article by id
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteArticle(@PathParam("id")int id) {
        this.articleService.deleteArticle(id);
    }
}