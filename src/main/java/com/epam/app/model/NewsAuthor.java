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

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;

        NewsAuthor that = (NewsAuthor) object;

        if (newsId != null ? !newsId.equals(that.newsId) : that.newsId != null)
            return false;
        return authorId != null ? authorId.equals(that.authorId) : that.authorId == null;

    }

    @Override
    public int hashCode() {
        int result = newsId != null ? newsId.hashCode() : 0;
        result = 31 * result + (authorId != null ? authorId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewsAuthor{" +
                "newsId=" + newsId +
                ", authorId=" + authorId +
                '}';
    }
}
