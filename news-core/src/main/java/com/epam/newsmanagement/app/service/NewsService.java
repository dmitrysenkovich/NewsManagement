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
    News add(News news, Author author, List<Tag> tags) throws ServiceException;

    /**
     * Retrieves all news specified
     * by searchCriteria object contents.
     * @param searchCriteria search criteria.
     * @return all fit news.
     * @throws ServiceException
     */
    List<News> search(SearchCriteria searchCriteria) throws ServiceException;

    /**
     * Retrieves all news
     * from database sorted by
     * comments count.
     * @return all news sorted by comments count.
     * @throws ServiceException
     */
    List<News> findAllSorted() throws ServiceException;

    /**
     * Counts all news.
     * @return news count. -1 if not
     * finished successfully.
     * @throws ServiceException
     */
    Long countAll() throws ServiceException;
}
