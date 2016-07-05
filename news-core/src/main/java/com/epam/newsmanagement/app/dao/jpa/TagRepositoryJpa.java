package com.epam.newsmanagement.app.dao.jpa;

import com.epam.newsmanagement.app.dao.TagRepository;
import com.epam.newsmanagement.app.dao.jpa.custom.TagRepositoryJpaCustom;
import com.epam.newsmanagement.app.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Tag repository.
 */
public interface TagRepositoryJpa extends TagRepository, JpaRepository<Tag, Long>, TagRepositoryJpaCustom {
    /**
     * Checks if tag exists.
     * @param tagName tagName
     * of the tag to be checked.
     * @return check result.
     */
    @Query("select case when count(*) > 0 then True else False end from Tag T where T.tagName = :tagName")
    boolean exists(@Param("tagName") String tagName);
}
