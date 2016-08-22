package com.epam.newsmanagement.app.utils;

import java.io.Serializable;
import java.util.List;

/**
 * SearchCriteria class. Used
 * to encapsulate search parameters.
 */
public class SearchCriteria implements Serializable {
    private List<Long> authorIds;
    private List<Long> tagIds;
    private Long pageIndex;
    private Long pageSize;

    public List<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<Long> authorIds) {
        this.authorIds = authorIds;
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
