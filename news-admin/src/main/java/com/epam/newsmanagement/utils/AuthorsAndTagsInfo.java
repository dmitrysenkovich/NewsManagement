package com.epam.newsmanagement.utils;

import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.Tag;

import java.util.List;

/**
 * Tags and authors in dropdown.
 */
public class AuthorsAndTagsInfo {
    private List<Author> notExpiredAuthors;
    private List<Tag> tags;

    public List<Author> getNotExpiredAuthors() {
        return notExpiredAuthors;
    }

    public void setNotExpiredAuthors(List<Author> notExpiredAuthors) {
        this.notExpiredAuthors = notExpiredAuthors;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
