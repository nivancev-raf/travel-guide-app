package com.raf.rafvodic.services;

import com.raf.rafvodic.entities.Article;
import com.raf.rafvodic.entities.Destination;
import com.raf.rafvodic.repositories.article.ArticleRepository;

import javax.inject.Inject;
import java.util.List;

public class ArticleService {


    @Inject
    ArticleRepository articleRepository;

    public ArticleService() {
    }


    public Article addArticle(Article article) {
        return articleRepository.addArticle(article);
    }

    public Article findArticleById(int id) {
        return articleRepository.findArticleById(id);
    }

    public List<Article> getAllArticles(int page, int limit) {
        int offset = (page - 1) * limit;
        return articleRepository.getAllArticles(offset, limit);
    }
    public List<Article> getMostReadArticles(int limit, String sort, String filter) {
        return articleRepository.findMostReadArticles(limit, sort, filter);
    }

    // get articles by activity id
    public List<Article> getArticlesByActivityId(int id) {
        return articleRepository.getArticlesByActivityId(id);
    }

    public Article updateArticle(Article article) {
        return articleRepository.updateArticle(article);
    }

    public void deleteArticle(int id) {
        articleRepository.deleteArticle(id);
    }

    public List<Article> getArticlesByDestinationId(int id) {
        return articleRepository.getArticlesByDestinationId(id);
    }

}
