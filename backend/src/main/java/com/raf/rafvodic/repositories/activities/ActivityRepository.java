package com.raf.rafvodic.repositories.activities;

import com.raf.rafvodic.entities.Activity;
import com.raf.rafvodic.entities.ArticleActivity;

import java.util.List;

public interface ActivityRepository {

    /*
    *     public Article addArticle(Article article);
    public Article findArticleById(Long id);
    public Article findArticleByName(String name);
    public List<Article> getAllArticles();
    public Article updateArticle(Article article);
    public void deleteArticle(Long id);
    * */
    public Activity addActivity(Activity activity);
    public ArticleActivity addArticleActivity(ArticleActivity activity);
    public Activity findActivityById(int id);
    public Activity findActivityByName(String name);
    public Activity updateActivity(Activity activity);
    public List<Activity> getAllActivities();
    public void deleteActivity(int id);
    public Activity findActivityByDestinationId(Long id);
    public Activity findActivityByDestinationName(String name);
    public List<Activity> getActivitiesByArticleId(int id);

    public void deleteActivitiesByArticleId(int id);

}
