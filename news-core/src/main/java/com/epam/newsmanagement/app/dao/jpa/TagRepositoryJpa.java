package com.epam.newsmanagement.app.dao.jpa;

import com.epam.newsmanagement.app.dao.TagRepository;
import com.epam.newsmanagement.app.dao.jpa.custom.TagRepositoryJpaCustom;
import com.epam.newsmanagement.app.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Tag repository.
 */
public interface TagRepositoryJpa extends TagRepository, JpaRepository<Tag, Long>, TagRepositoryJpaCustom {
}
