package com.epam.app.model;

/**
 * Tag model.
 */
public class Tag {
    private Long tagId;
    private String tagName;

    public Tag() {}

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
