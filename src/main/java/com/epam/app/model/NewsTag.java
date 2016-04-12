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

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;

        NewsTag newsTag = (NewsTag) object;

        if (newsId != null ? !newsId.equals(newsTag.newsId) : newsTag.newsId != null)
            return false;
        return tagId != null ? tagId.equals(newsTag.tagId) : newsTag.tagId == null;

    }

    @Override
    public int hashCode() {
        int result = newsId != null ? newsId.hashCode() : 0;
        result = 31 * result + (tagId != null ? tagId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewsTag{" +
                "newsId=" + newsId +
                ", tagId=" + tagId +
                '}';
    }
}
