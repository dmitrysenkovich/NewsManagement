package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;

import java.util.List;

/**
 * Tag service interface.
 */
public interface TagService extends CrudService<Tag> {
    /**
     * Returns news tags.
     * @param news specifies news
     * which tags are to be retrieved.
     * @return news tags.
     * @throws ServiceException
     */
    default List<Tag> findAllByNews(News news) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Returns all tags.
     * @return all tags.
     * @throws ServiceException
     */
    default List<Tag> findAll() throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Checks if tag exists.
     * @param tag tag to be checked.
     * @return check result.
     * @throws ServiceException
     */
    default boolean exists(Tag tag) throws ServiceException {
        throw new ServiceException();
    }
}
