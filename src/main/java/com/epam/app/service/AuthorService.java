package com.epam.app.service;

import com.epam.app.model.Author;

/**
 * Author service interface.
 */
public interface AuthorService extends RudService<Author> {
    /**
     * Adds new author.
     * @param author new author.
     * @return this new author with id set
     * if added successfully.
     */
    Author add(Author author);
}
