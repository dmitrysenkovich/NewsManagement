package com.epam.newsmanagement.app.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * News model.
 */
public class News {
    private Long newsId;
    private String title;
    private String shortText;
    private String fullText;
    private Timestamp creationDate;
    private Date modificationDate;

    public News() {}

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;

        News news = (News) object;

        if (newsId != null ? !newsId.equals(news.newsId) : news.newsId != null)
            return false;
        if (title != null ? !title.equals(news.title) : news.title != null)
            return false;
        if (shortText != null ? !shortText.equals(news.shortText) : news.shortText != null)
            return false;
        if (fullText != null ? !fullText.equals(news.fullText) : news.fullText != null)
            return false;
        if (creationDate != null ? !creationDate.equals(news.creationDate) : news.creationDate != null)
            return false;
        return modificationDate != null ? modificationDate.equals(news.modificationDate) : news.modificationDate == null;

    }

    @Override
    public int hashCode() {
        int result = newsId != null ? newsId.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (shortText != null ? shortText.hashCode() : 0);
        result = 31 * result + (fullText != null ? fullText.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (modificationDate != null ? modificationDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "News{" +
                "newsId=" + newsId +
                ", title='" + title + '\'' +
                ", shortText='" + shortText + '\'' +
                ", fullText='" + fullText + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                '}';
    }
}
