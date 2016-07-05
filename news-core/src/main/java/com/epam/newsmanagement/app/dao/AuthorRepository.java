package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;

import java.util.List;

/**
 * Root interface for author repositories.
 */
public interface AuthorRepository extends CrudRepository<Author, Long> {

    /**
     * Returns news authors.
     * @param news specifies news
     * which authors are to be retrieved.
     * @return news authors.
     */
    List<Author> findAllByNews(News news);

    /**
     * Returns all not expired authors.
     * @return all not expired authors.
     */
    List<Author> findNotExpired();

    /**
     * Returns all authors.
     * @return all authors.
     */
    List<Author> findAll();

    /**
     * Checks if author exists.
     * @param authorName author name to be checked.
     * @return check result.
     */
    boolean exists(String authorName);

    /**
     * Adds news to author relations
     * for each author in passed list.
     * @param news news.
     * @param authors authors.
     */
    void addAll(News news, List<Author> authors);

    /**
     * Deletes all authors relations
     * by passed news.
     * @param news relations with
     * this news will be deleted.
     */
    void deleteAllRelationsByNews(News news);
}
