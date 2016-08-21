package com.epam.newsmanagement.app.dao.jdbc;

import com.epam.newsmanagement.app.dao.AuthorRepository;
import com.epam.newsmanagement.app.exception.NotImplementedException;
import com.epam.newsmanagement.app.model.Author;

/**
 * Author repository interface.
 */
public interface AuthorRepositoryJdbc extends AuthorRepository {
    /**
     * Forbids author deleting.
     * @param author author.
     */
    @Override
    default void delete(Author author) {
        throw new NotImplementedException("");
    }
}
