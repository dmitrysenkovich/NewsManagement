package com.epam.app.service;

import com.epam.app.dao.NewsTagRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.exception.ServiceException;
import com.epam.app.model.News;
import com.epam.app.model.NewsTag;
import com.epam.app.model.Tag;
import com.epam.app.service.impl.NewsTagServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

/**
 * NewsTag service test.
 */
public class NewsTagServiceTest {
    @InjectMocks
    private NewsTagServiceImpl newsTagService;

    @Mock
    private NewsTagRepository newsTagRepository;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void added() throws Exception {
        News news = new News();
        Tag tag = new Tag();
        doNothing().when(newsTagRepository).add(any(NewsTag.class));
        newsTagService.add(news, tag);
    }


    @Test(expected = ServiceException.class)
    public void notAdded() throws Exception {
        News news = new News();
        Tag tag = new Tag();
        doThrow(new DaoException()).when(newsTagRepository).add(any(NewsTag.class));
        newsTagService.add(news, tag);
    }


    @Test
    public void deleted() throws Exception {
        News news = new News();
        Tag tag = new Tag();
        doNothing().when(newsTagRepository).delete(any(NewsTag.class));
        newsTagService.add(news, tag);
    }


    @Test(expected = ServiceException.class)
    public void notDeleted() throws Exception {
        News news = new News();
        Tag tag = new Tag();
        doThrow(new DaoException()).when(newsTagRepository).delete(any(NewsTag.class));
        newsTagService.delete(news, tag);
    }
}
