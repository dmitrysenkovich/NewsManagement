package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * News repository interface.
 */
public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {
    /**
     * Counts all news.
     * @return news count.
     */
    @Query("select count(*) from News")
    Long countAll();

    /**
     * Deletes all news with id from list.
     * @param newsIds ids of news to be deleted.
     */
    @Modifying
    @Query("delete from News where newsId in :newsIds")
    void deleteAll(@Param("newsIds") List<Long> newsIds);
}
