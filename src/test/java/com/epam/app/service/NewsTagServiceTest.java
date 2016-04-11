package com.epam.app.service;

import com.epam.app.dao.NewsTagRepository;
import com.epam.app.model.News;
import com.epam.app.model.NewsTag;
import com.epam.app.model.Tag;
import com.epam.app.service.impl.NewsTagServiceImpl;
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
 * NewsTag service test.
 */
public class NewsTagServiceTest {
    @InjectMocks
    private NewsTagServiceImpl newsTagService;

    @Mock
    private NewsTagRepository newsTagRepository;

    @Mock
    private Logger logger;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(NewsTagServiceImpl.class, "logger", logger);
    }

    @Test
    public void added() {
        News news = new News();
        Tag tag = new Tag();
        when(newsTagRepository.add(any(NewsTag.class))).thenReturn(true);
        boolean added = newsTagService.add(news, tag);

        assertTrue(added);
        verify(logger).info(eq("Successfully added new tag to news"));
    }

    @Test
    public void notAdded() {
        News news = new News();
        Tag tag = new Tag();
        when(newsTagRepository.add(any(NewsTag.class))).thenReturn(false);
        boolean added = newsTagService.add(news, tag);

        assertFalse(added);
        verify(logger).error(eq("Failed to add tag to news"));
    }

    @Test
    public void deleted() {
        News news = new News();
        Tag tag = new Tag();
        when(newsTagRepository.delete(any(NewsTag.class))).thenReturn(true);
        boolean deleted = newsTagService.delete(news, tag);

        assertTrue(deleted);
        verify(logger).info(eq("Successfully deleted tag from news"));
    }

    @Test
    public void notDeleted() {
        News news = new News();
        Tag tag = new Tag();
        when(newsTagRepository.delete(any(NewsTag.class))).thenReturn(false);
        boolean deleted = newsTagService.delete(news, tag);

        assertFalse(deleted);
        verify(logger).error(eq("Failed to delete tag from news"));
    }
}
