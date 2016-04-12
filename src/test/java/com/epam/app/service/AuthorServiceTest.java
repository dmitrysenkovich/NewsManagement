package com.epam.app.service;

import com.epam.app.dao.AuthorRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.exception.ServiceException;
import com.epam.app.model.Author;
import com.epam.app.service.impl.AuthorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Author service test.
 */
public class AuthorServiceTest {
    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void added() throws Exception {
        Author author = new Author();
        author.setAuthorId(1L);
        when(authorRepository.add(author)).thenReturn(1L);
        author = authorService.add(author);

        assertEquals((Long) 1L, author.getAuthorId());
    }


    @Test(expected = ServiceException.class)
    public void notAdded() throws Exception {
        doThrow(new DaoException()).when(authorRepository).add(any(Author.class));
        authorService.add(new Author());
    }


    @Test
    public void found() throws Exception {
        Author author = new Author();
        author.setAuthorId(1L);
        when(authorRepository.find(1L)).thenReturn(author);
        author = authorService.find(1L);

        assertEquals((Long) 1L, author.getAuthorId());
    }


    @Test(expected = ServiceException.class)
    public void notFound() throws Exception {
        doThrow(new DaoException()).when(authorRepository).find(any(Long.class));
        authorService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        doNothing().when(authorRepository).update(any(Author.class));
        authorService.update(new Author());
    }


    @Test(expected = ServiceException.class)
    public void notUpdated() throws Exception {
        doThrow(new DaoException()).when(authorRepository).update(any(Author.class));
        authorService.update(new Author());
    }


    @Test
    public void deleted() throws Exception {
        doNothing().when(authorRepository).delete(any(Author.class));
        authorService.delete(new Author());
    }


    @Test(expected = ServiceException.class)
    public void notDeleted() throws Exception {
        doThrow(new DaoException()).when(authorRepository).delete(any(Author.class));
        authorService.delete(new Author());
    }
}
