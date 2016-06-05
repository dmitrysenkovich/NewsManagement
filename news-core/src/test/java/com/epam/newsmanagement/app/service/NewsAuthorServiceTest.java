package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.dao.NewsAuthorRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.NewsAuthor;
import com.epam.newsmanagement.app.service.impl.NewsAuthorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

/**
 * NewsAuthor service test.
 */
public class NewsAuthorServiceTest {
    @InjectMocks
    private NewsAuthorServiceImpl newsAuthorService;

    @Mock
    private NewsAuthorRepository newsAuthorRepository;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void added() throws Exception {
        News news = new News();
        Author author = new Author();
        doNothing().when(newsAuthorRepository).add(any(NewsAuthor.class));
        newsAuthorService.add(news, author);
    }


    @Test(expected = ServiceException.class)
    public void didNotAdd() throws Exception {
        News news = new News();
        Author author = new Author();
        doThrow(new DaoException()).when(newsAuthorRepository).add(any(NewsAuthor.class));
        newsAuthorService.add(news, author);
    }


    @Test
    public void deleted() throws Exception {
        News news = new News();
        Author author = new Author();
        doNothing().when(newsAuthorRepository).delete(any(NewsAuthor.class));
        newsAuthorService.add(news, author);
    }


    @Test(expected = ServiceException.class)
    public void didNotDelete() throws Exception {
        News news = new News();
        Author author = new Author();
        doThrow(new DaoException()).when(newsAuthorRepository).delete(any(NewsAuthor.class));
        newsAuthorService.delete(news, author);
    }
}
