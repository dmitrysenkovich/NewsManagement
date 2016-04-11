package com.epam.app.service;

import com.epam.app.dao.NewsAuthorRepository;
import com.epam.app.model.Author;
import com.epam.app.model.News;
import com.epam.app.model.NewsAuthor;
import com.epam.app.service.impl.NewsAuthorServiceImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * NewsAuthor service test.
 */
public class NewsAuthorServiceTest {
    @InjectMocks
    private NewsAuthorServiceImpl newsAuthorService;

    @Mock
    private NewsAuthorRepository newsAuthorRepository;

    @Mock
    private Logger logger;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(NewsAuthorServiceImpl.class, "logger", logger);
    }

    @Test
    public void added() {
        News news = new News();
        Author author = new Author();
        when(newsAuthorRepository.add(any(NewsAuthor.class))).thenReturn(true);
        boolean added = newsAuthorService.add(news, author);

        assertTrue(added);
        verify(logger).info(eq("Successfully added new author to news"));
    }

    @Test
    public void notAdded() {
        News news = new News();
        Author author = new Author();
        when(newsAuthorRepository.add(any(NewsAuthor.class))).thenReturn(false);
        boolean added = newsAuthorService.add(news, author);

        assertFalse(added);
        verify(logger).error(eq("Failed to add author to news"));
    }

    @Test
    public void deleted() {
        News news = new News();
        Author author = new Author();
        when(newsAuthorRepository.delete(any(NewsAuthor.class))).thenReturn(true);
        boolean deleted = newsAuthorService.delete(news, author);

        assertTrue(deleted);
        verify(logger).info(eq("Successfully deleted author from news"));
    }

    @Test
    public void notDeleted() {
        News news = new News();
        Author author = new Author();
        when(newsAuthorRepository.delete(any(NewsAuthor.class))).thenReturn(false);
        boolean deleted = newsAuthorService.delete(news, author);

        assertFalse(deleted);
        verify(logger).error(eq("Failed to delete author from news"));
    }
}
