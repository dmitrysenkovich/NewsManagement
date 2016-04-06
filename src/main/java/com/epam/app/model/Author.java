package com.epam.app.model;

import java.sql.Timestamp;

/**
 * Author model.
 */
public class Author {
    private int authorId;
    private String authorName;
    private Timestamp expired;

    public Author() {}

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
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
}
