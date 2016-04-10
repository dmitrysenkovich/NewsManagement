package com.epam.app.utils;

import java.util.List;

/**
 * SearchCriteria class. Used
 * to encapsulate search parameters.
 */
public class SearchCriteria {
    private Long authorId;
    private List<Long> tagIds;

    public SearchCriteria() {}

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
}
