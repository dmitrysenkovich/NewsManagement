package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.exception.DaoException;
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
     * @throws DaoException
     */
    default List<Tag> getAllByNews(News news) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Returns all tags.
     * @return all tags.
     * @throws ServiceException
     */
    default List<Tag> getAll() throws ServiceException {
        throw new ServiceException();
    }
}
