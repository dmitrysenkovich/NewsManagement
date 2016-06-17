package com.epam.newsmanagement.app.dao;

import com.epam.newsmanagement.app.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Tag repository.
 */
public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {
    /**
     * Checks if tag exists.
     * @param tagName tagName
     * of the tag to be checked.
     * @return check result.
     */
    @Query("select case when count(*) > 0 then True else False end from Tag T where T.tagName = :tagName")
    boolean exists(@Param("tagName") String tagName);
}
