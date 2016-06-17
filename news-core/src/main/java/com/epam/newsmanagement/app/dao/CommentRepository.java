package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Comment repository interface.
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    /**
     * Deletes all comments from list.
     * @param comments comments to be deleted.
     */
    @Query("delete from Comment C where C in (:comments)")
    void deleteAll(@Param("comments") List<Comment> comments);

    /**
     * Counts news comments count.
     * @param news news which comments
     * will be counted.
     * @return news comments count.
     */
    @Query("select count(*) from Comment where news = :news")
    Long countAllByNews(@Param("news") News news);
}
