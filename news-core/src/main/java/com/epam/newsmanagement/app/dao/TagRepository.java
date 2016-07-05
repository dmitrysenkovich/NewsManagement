package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;

import java.util.List;

/**
 * Root interface for tag repositories.
 */
public interface TagRepository extends CrudRepository<Tag, Long> {
    /**
     * Returns news tags.
     * @param news specifies news
     * which tags are to be retrieved.
     * @return news tags.
     */
    List<Tag> findAllByNews(News news);

    /**
     * Returns all tags.
     * @return all tags.
     */
    List<Tag> findAll();

    /**
     * Checks if tag exists.
     * @param tagName tag name to be checked.
     * @return check result.
     */
    boolean exists(String tagName);

    /**
     * Adds news to tag relations
     * for each tag in passed list.
     * @param news news.
     * @param tags tags.
     */
    void addAll(News news, List<Tag> tags);

    /**
     * Deletes all tags relations
     * by passed news.
     * @param news relations with
     * this news will be deleted.
     */
    void deleteAllRelationsByNews(News news);
}
