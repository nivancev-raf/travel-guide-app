package com.raf.rafvodic.repositories.article;

import com.raf.rafvodic.entities.Article;

import java.util.List;

public interface ArticleRepository {

    public Article addArticle(Article article);
    public Article findArticleById(int id);
    public List<Article> getAllArticles(int offset, int limit);
    public List<Article> getArticlesByActivityId(int id);
    // findMostReadArticles
    public List<Article> findMostReadArticles(int limit, String sort, String filter);
    public Article updateArticle(Article article);
    public void deleteArticle(int id);
    public List<Article> getArticlesByDestinationId(int id);


}
