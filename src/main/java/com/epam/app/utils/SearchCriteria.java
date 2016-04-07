package com.epam.app.utils;

import java.util.List;

/**
 * SearchCriteria class. Used
 * to encapsulate search parameters.
 */
public class SearchCriteria {
    private Integer authorId;
    private List<Integer> tagIds;

    public SearchCriteria() {}

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }
}
