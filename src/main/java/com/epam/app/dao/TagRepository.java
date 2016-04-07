package com.epam.app.dao;

import com.epam.app.model.Tag;

import java.util.List;

/**
 * Tag repository.
 */
public interface TagRepository extends CrudRepository<Tag> {
    /**
     * Adds all tags from list.
     * @param tags tags to be added.
     * @return tags with set id
     * if successfully.
     */
    List<Tag> addTags(List<Tag> tags);
}
