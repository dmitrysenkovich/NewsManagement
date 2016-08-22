package com.epam.newsmanagement.app.dao.jpa;

import com.epam.newsmanagement.app.dao.NewsRepository;
import com.epam.newsmanagement.app.dao.jpa.custom.NewsRepositoryJpaCustom;
import com.epam.newsmanagement.app.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * News repository interface.
 */
public interface NewsRepositoryJpa extends NewsRepository, JpaRepository<News, Long>, NewsRepositoryJpaCustom {
    /**
     * Counts all news.
     * @return news count.
     */
    @Override
    @Query("select count(N) from News N")
    Long countAll();

    /**
     * Deletes all news with id from list.
     * @param newsIds ids of news to be deleted.
     */
    @Override
    @Modifying
    @Query("delete from News N where N.newsId in :newsIds")
    void deleteAll(@Param("newsIds") List<Long> newsIds);
}
