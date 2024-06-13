package com.raf.rafvodic.entities;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class Comment {

    @NotNull
    private int id;
    @NotNull
    private String author;
    @NotNull
    private String text;
    @NotNull
    private int articleId;
    @NotNull
    private Timestamp creationTime;

    public Comment() {
    }

    public Comment(int id, String author, String text, int articleId, Timestamp creationTime) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.articleId = articleId;
        this.creationTime = creationTime;
    }

    public Comment(String author, String text, int articleId, Timestamp creationTime) {
        this.author = author;
        this.text = text;
        this.articleId = articleId;
        this.creationTime = creationTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }
}
