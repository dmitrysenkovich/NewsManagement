package com.epam.app.model;

/**
 * NewsAuthor model.
 */
public class NewsAuthor {
    private Long newsId;
    private Long authorId;

    public NewsAuthor() {}

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
