package com.epam.app.model;

/**
 * NewsAuthor model.
 */
public class NewsAuthor {
    private int newsId;
    private int authorId;

    public NewsAuthor() {}

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
}
