package com.epam.newsmanagement.app.dao.jpa;

import com.epam.newsmanagement.app.dao.AuthorRepository;
import com.epam.newsmanagement.app.dao.jpa.custom.AuthorRepositoryJpaCustom;
import com.epam.newsmanagement.app.exception.NotImplementedException;
import com.epam.newsmanagement.app.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Author repository interface.
 */
public interface AuthorRepositoryJpa extends AuthorRepository, JpaRepository<Author, Long>, AuthorRepositoryJpaCustom {
    /**
     * Returns all not expired authors.
     * @return all not expired authors.
     */
    @Override
    @Query("from Author A where A.expired is null")
    List<Author> findNotExpired();

    /**
     * Forbids author deleting.
     * @param author author.
     */
    @Override
    default void delete(Author author) {
        throw new NotImplementedException("");
    }
}
