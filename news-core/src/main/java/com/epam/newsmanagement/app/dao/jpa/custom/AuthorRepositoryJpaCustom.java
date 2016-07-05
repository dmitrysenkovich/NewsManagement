package com.epam.newsmanagement.app.dao.jpa.custom;

import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;

import java.util.List;

/**
 * Interface with methods
 * implemented manually.
 */
public interface AuthorRepositoryJpaCustom {
    /**
     * Returns news authors.
     * @param news specifies news
     * which authors are to be retrieved.
     * @return news authors.
     */
    List<Author> findAllByNews(News news);

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
