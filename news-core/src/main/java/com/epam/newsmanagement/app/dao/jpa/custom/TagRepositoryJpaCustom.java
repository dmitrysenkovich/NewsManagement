package com.epam.newsmanagement.app.dao.jpa.custom;

import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;

import java.util.List;

/**
 * Interface with methods
 * implemented manually.
 */
public interface TagRepositoryJpaCustom {
    /**
     * Checks if tag exists.
     * @param tagName tagName
     * of the tag to be checked.
     * @return check result.
     */
    boolean exists(String tagName);

    /**
     * Returns news tags.
     * @param news specifies news
     * which tags are to be retrieved.
     * @return news tags.
     */
    List<Tag> findAllByNews(News news);

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
