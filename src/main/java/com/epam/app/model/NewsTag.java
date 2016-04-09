package com.epam.app.model;

/**
 * NewsTag model.
 */
public class NewsTag {
    private Long newsId;
    private Long tagId;

    public NewsTag() {}

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
