package com.epam.newsmanagement.app.model;

import java.sql.Timestamp;

/**
 * Author model.
 */
public class Author {
    private Long authorId;
    private String authorName;
    private Timestamp expired;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Timestamp getExpired() {
        return expired;
    }

    public void setExpired(Timestamp expired) {
        this.expired = expired;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;

        Author author = (Author) object;

        if (authorId != null ? !authorId.equals(author.authorId) : author.authorId != null)
            return false;
        if (authorName != null ? !authorName.equals(author.authorName) : author.authorName != null)
            return false;
        return expired != null ? expired.equals(author.expired) : author.expired == null;

    }

    @Override
    public int hashCode() {
        int result = authorId != null ? authorId.hashCode() : 0;
        result = 31 * result + (authorName != null ? authorName.hashCode() : 0);
        result = 31 * result + (expired != null ? expired.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", expired=" + expired +
                '}';
    }
}
