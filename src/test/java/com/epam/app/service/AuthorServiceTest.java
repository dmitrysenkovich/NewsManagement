package com.epam.app.service;

import com.epam.app.dao.AuthorRepository;
import com.epam.app.dao.impl.AuthorRepositoryImpl;
import com.epam.app.model.Author;
import com.epam.app.service.impl.AuthorServiceImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.powermock.reflect.Whitebox;

/**
 * Author service test.
 */
public class AuthorServiceTest {
    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private Logger logger;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(AuthorServiceImpl.class, "logger", logger);
    }

    @Test
    public void added() {
        Author author = new Author();
        author.setAuthorId(1L);
        when(authorRepository.add(author)).thenReturn(author);
        author = authorService.add(author);

        assertEquals((Long) 1L, author.getAuthorId());
        verify(logger).info(eq("Successfully added new author"));
    }

    @Test
    public void notAdded() {
        Author author = new Author();
        when(authorRepository.add(author)).thenReturn(author);
        author = authorService.add(author);

        assertNull(author.getAuthorId());
        verify(logger).error(eq("Failed to add new author"));
    }

    @Test
    public void found() {
        Author author = new Author();
        author.setAuthorId(1L);
        when(authorRepository.find(1L)).thenReturn(author);
        author = authorService.find(1L);

        assertEquals((Long) 1L, author.getAuthorId());
        verify(logger).info(eq("Successfully found author"));
    }

    @Test
    public void notFound() {
        when(authorRepository.find(1L)).thenReturn(null);
        Author author = authorService.find(1L);

        assertNull(author);
        verify(logger).error(eq("Failed to find author"));
    }

    @Test
    public void updated() {
        Author author = new Author();
        author.setAuthorId(1L);
        when(authorRepository.update(author)).thenReturn(true);
        boolean updated = authorService.update(author);

        assertTrue(updated);
        verify(logger).info(eq("Successfully updated author"));
    }

    @Test
    public void notUpdated() {
        when(authorRepository.update(null)).thenReturn(false);
        boolean updated = authorService.update(null);

        assertFalse(updated);
        verify(logger).error(eq("Failed to update author"));
    }

    @Test
    public void deleted() {
        Author author = new Author();
        author.setAuthorId(1L);
        when(authorRepository.delete(author)).thenReturn(true);
        boolean deleted = authorService.delete(author);

        assertTrue(deleted);
        verify(logger).info(eq("Successfully deleted author"));
    }

    @Test
    public void notDeleted() {
        when(authorRepository.delete(null)).thenReturn(false);
        boolean deleted = authorService.delete(null);

        assertFalse(deleted);
        verify(logger).error(eq("Failed to delete author"));
    }
}
