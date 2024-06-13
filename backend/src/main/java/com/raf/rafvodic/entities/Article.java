package com.raf.rafvodic.entities;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Article {
    @NotNull
    private int id;
    @NotNull
    private String title;
    @NotNull
    private String text;
    @NotNull
    private Timestamp creationTime;
    @NotNull
    @Min(0)
    private int visitCount;
    @NotNull
    private int authorId;
    @NotNull
    private int destinationId;

    // Constructors
    public Article() {}

    public Article(String title, String text, int authorId, int destinationId, Timestamp creationTime) {
        this.title = title;
        this.text = text;
        this.creationTime = creationTime;
        this.visitCount = 0;
        this.authorId = authorId;
        this.destinationId = destinationId;
    }

    public Article(int id, String title, String text, Timestamp creationTime, int visitCount, int authorId, int destinationId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.creationTime = creationTime;
        this.visitCount = visitCount;
        this.authorId = authorId;
        this.destinationId = destinationId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }
}

