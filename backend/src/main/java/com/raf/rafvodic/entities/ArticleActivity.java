package com.raf.rafvodic.entities;

import javax.validation.constraints.NotNull;

public class ArticleActivity {

    @NotNull
    private int articleId;
    @NotNull
    private int activityId;

    public ArticleActivity() {
    }

    public ArticleActivity(int articleId, int activityId) {
        this.articleId = articleId;
        this.activityId = activityId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
}
