package com.epam.app.model;

/**
 * NewsTag model.
 */
public class NewsTag {
    private int newsId;
    private int tagId;

    public NewsTag() {}

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
