package com.epam.newsmanagement.app.utils;

import java.util.List;

/**
 * SearchCriteria class. Used
 * to encapsulate search parameters.
 */
public class SearchCriteria {
    private Long authorId;
    private List<Long> tagIds;
    private Long pageIndex;
    private Long pageSize;

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

    public Long getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }
}
