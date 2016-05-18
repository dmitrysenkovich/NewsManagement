package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.utils.SearchCriteria;

import java.util.List;

/**
 * News service interface
 */
public interface NewsService extends CrudService<News> {
    /**
     * Adds news.
     * @param news news.
     * @param author news author.
     * @param tags news tags.
     * @return this news with id set
     * if added successfully.
     * @throws ServiceException
     */
    default News add(News news, Author author, List<Tag> tags) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Retrieves all news specified
     * by searchCriteria object contents.
     * @param searchCriteria search criteria.
     * @return all fit news.
     * @throws ServiceException
     */
    default List<News> search(SearchCriteria searchCriteria) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Retrieves all news
     * from database sorted by
     * comments count.
     * @return all news sorted by comments count.
     * @throws ServiceException
     */
    default List<News> findAllSorted() throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Counts all news.
     * @return news count. -1 if not
     * finished successfully.
     * @throws ServiceException
     */
    default Long countAll() throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Counts news pages satisfying
     * search criteria.
     * @param searchCriteria search criteria.
     * @return news count.
     */
    default Long countPagesBySearchCriteria(SearchCriteria searchCriteria) throws ServiceException {
        throw new ServiceException();
    }

    /**
     * Deletes all news with id from list.
     * @param newsIds ids of news to be deleted.
     * @throws ServiceException
     */
    default void deleteAll(List<Long> newsIds) throws ServiceException {
        throw new ServiceException();
    }
}
