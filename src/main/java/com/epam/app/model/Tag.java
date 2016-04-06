package com.epam.app.model;

/**
 * Tag model.
 */
public class Tag {
    private int tagId;
    private int tagName;

    public Tag() {}

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getTagName() {
        return tagName;
    }

    public void setTagName(int tagName) {
        this.tagName = tagName;
    }
}
