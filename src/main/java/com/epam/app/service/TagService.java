package com.epam.app.service;

import com.epam.app.model.Tag;

import java.util.List;

/**
 * Tag service interface.
 */
public interface TagService extends RudService<Tag> {
    /**
     * Adds tag.
     * @param tag new tag.
     * @return tag with set id
     * if added successfully.
     */
    Tag add(Tag tag);

    /**
     * Adds all tags from list to news.
     * @param tags tags to be added.
     * @return tags with set id
     * if successfully.
     */
    List<Tag> addAll(List<Tag> tags);
}
