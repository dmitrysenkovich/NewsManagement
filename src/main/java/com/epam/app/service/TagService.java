package com.epam.app.service;

import com.epam.app.exception.ServiceException;
import com.epam.app.model.Tag;

import java.util.List;

/**
 * Tag service interface.
 */
public interface TagService extends CrudService<Tag> {
    /**
     * Adds all tags from list to news.
     * @param tags tags to be added.
     * @return tags with set id
     * if successfully.
     * @throws ServiceException
     */
    List<Tag> addAll(List<Tag> tags) throws ServiceException;
}
