package com.epam.app.service;

import com.epam.app.dao.NewsAuthorRepository;
import com.epam.app.dao.NewsRepository;
import com.epam.app.dao.NewsTagRepository;
import com.epam.app.model.Author;
import com.epam.app.model.News;
import com.epam.app.model.NewsAuthor;
import com.epam.app.model.NewsTag;
import com.epam.app.model.Tag;
import com.epam.app.service.impl.NewsServiceImpl;
import com.epam.app.utils.SearchCriteria;
import com.epam.app.utils.SearchUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * News service test.
 */
@RunWith(PowerMockRunner.class)
public class NewsServiceTest {
    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsRepository newsRepository;
    @Mock
    private NewsAuthorRepository newsAuthorRepository;
    @Mock
    private NewsTagRepository newsTagRepository;

    @Mock
    private SearchUtils searchUtils;

    @Mock
    private Logger logger;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(NewsServiceImpl.class, "logger", logger);
    }

    @Test
    public void addedNewsAuthorIsNullTagsAreNull() {
        News news = new News();
        news.setNewsId(1L);
        when(newsRepository.add(news)).thenReturn(news);
        news = newsService.add(news, null, null);

        assertEquals((Long) 1L, news.getNewsId());
        verify(logger).info(eq("Successfully added news"));
        verify(logger, never()).info(eq("Adding author to news.."));
        verify(logger, never()).info(eq("Adding tags to news.."));
    }

    @Test
    public void addedNewsAuthorIsNotNullTagsAreNull() {
        News news = new News();
        news.setNewsId(1L);
        when(newsRepository.add(news)).thenReturn(news);
        when(newsAuthorRepository.add(any(NewsAuthor.class))).thenReturn(true);
        news = newsService.add(news, new Author(), null);

        assertEquals((Long) 1L, news.getNewsId());
        verify(logger).info(eq("Successfully added author to news"));
        verify(logger).info(eq("Successfully added news"));
        verify(logger, never()).error(eq("Failed to add author to news"));
        verify(logger, never()).info(eq("Adding tags to news.."));
    }

    @Test
    public void addedNewsAuthorIsNullTagsAreNotNull() {
        News news = new News();
        news.setNewsId(1L);
        List<Tag> tags = new LinkedList<Tag>();
        tags.add(new Tag());
        when(newsRepository.add(news)).thenReturn(news);
        when(newsTagRepository.add(any(NewsTag.class))).thenReturn(true);
        news = newsService.add(news, null, tags);

        assertEquals((Long) 1L, news.getNewsId());
        verify(logger).info(eq("Successfully added tags to news"));
        verify(logger).info(eq("Successfully added news"));
        verify(logger, never()).error(eq("Failed to add tags to news"));
        verify(logger, never()).info(eq("Adding author to news.."));
    }

    @Test
    public void addedNewsAuthorIsNotNullTagsAreNotNull() {
        News news = new News();
        news.setNewsId(1L);
        List<Tag> tags = new LinkedList<Tag>();
        tags.add(new Tag());
        when(newsRepository.add(news)).thenReturn(news);
        when(newsAuthorRepository.add(any(NewsAuthor.class))).thenReturn(true);
        when(newsTagRepository.add(any(NewsTag.class))).thenReturn(true);
        news = newsService.add(news, new Author(), tags);

        assertEquals((Long) 1L, news.getNewsId());
        verify(logger).info(eq("Successfully added author to news"));
        verify(logger).info(eq("Successfully added tags to news"));
        verify(logger).info(eq("Successfully added news"));
        verify(logger, never()).error(eq("Failed to add author to news"));
        verify(logger, never()).error(eq("Failed to add tags to news"));
    }

    @Test
    public void notAddedNewsAuthorIsNullTagsAreNullNewsFailure() {
        News news = new News();
        when(newsRepository.add(news)).thenReturn(news);
        news = newsService.add(news, null, null);

        assertNull(news.getNewsId());
        verify(logger).error(eq("Failed to add news"));
        verify(logger, never()).info(eq("Adding new author to news.."));
        verify(logger, never()).info(eq("Adding new tags to news.."));
    }

    @Test
    public void notAddedNewsAuthorIsNotNullTagsAreNullNewsFailure() {
        News news = new News();
        when(newsRepository.add(news)).thenReturn(news);
        news = newsService.add(news, new Author(), null);

        assertNull(news.getNewsId());
        verify(logger).error(eq("Failed to add news"));
        verify(logger, never()).info(eq("Adding new author to news.."));
        verify(logger, never()).info(eq("Adding new tags to news.."));
    }

    @Test
    public void notAddedNewsAuthorIsNullTagsAreNotNullNewsFailure() {
        News news = new News();
        List<Tag> tags = new LinkedList<Tag>();
        tags.add(new Tag());
        when(newsRepository.add(news)).thenReturn(news);
        news = newsService.add(news, null, tags);

        assertNull(news.getNewsId());
        verify(logger).error(eq("Failed to add news"));
        verify(logger, never()).info(eq("Adding new author to news.."));
        verify(logger, never()).info(eq("Adding new tags to news.."));
    }

    @Test
    public void notAddedNewsAuthorIsNotNullTagsAreNotNullNewsFailure() {
        News news = new News();
        List<Tag> tags = new LinkedList<Tag>();
        tags.add(new Tag());
        when(newsRepository.add(news)).thenReturn(news);
        news = newsService.add(news, new Author(), tags);

        assertNull(news.getNewsId());
        verify(logger).error(eq("Failed to add news"));
        verify(logger, never()).info(eq("Adding new author to news.."));
        verify(logger, never()).info(eq("Adding new tags to news.."));
    }

    @Test
    public void notAddedNewsTagsAreNullAuthorFailure() {
        News news = new News();
        news.setNewsId(1L);
        when(newsRepository.add(news)).thenReturn(news);
        when(newsAuthorRepository.add(any(NewsAuthor.class))).thenReturn(false);
        news = newsService.add(news, new Author(), null);

        assertEquals((Long) 1L, news.getNewsId());
        verify(logger).error(eq("Failed to add author to news"));
        verify(logger).error(eq("Failed to add news"));
        verify(logger, never()).info(eq("Successfully added author to news"));
        verify(logger, never()).info(eq("Adding new tags to news.."));
        verify(logger, never()).info(eq("Successfully added news"));
    }

    @Test
    public void notAddedNewsTagsAreNotNullAuthorFailure() {
        News news = new News();
        news.setNewsId(1L);
        List<Tag> tags = new LinkedList<Tag>();
        tags.add(new Tag());
        when(newsRepository.add(news)).thenReturn(news);
        when(newsAuthorRepository.add(any(NewsAuthor.class))).thenReturn(false);
        news = newsService.add(news, new Author(), tags);

        assertEquals((Long) 1L, news.getNewsId());
        verify(logger).error(eq("Failed to add author to news"));
        verify(logger).error(eq("Failed to add news"));
        verify(logger, never()).info(eq("Successfully added author to news"));
        verify(logger, never()).info(eq("Adding new tags to news.."));
        verify(logger, never()).info(eq("Successfully added news"));
    }

    @Test
    public void notAddedNewsAuthorIsNullTagsFailure() {
        News news = new News();
        news.setNewsId(1L);
        List<Tag> tags = new LinkedList<Tag>();
        tags.add(new Tag());
        when(newsRepository.add(news)).thenReturn(news);
        when(newsAuthorRepository.add(any(NewsAuthor.class))).thenReturn(true);
        when(newsTagRepository.add(any(NewsTag.class))).thenReturn(false);
        news = newsService.add(news, null, tags);

        assertEquals((Long) 1L, news.getNewsId());
        verify(logger).error(eq("Failed to add tags to news"));
        verify(logger).error(eq("Failed to add news"));
        verify(logger, never()).info(eq("Successfully added tags to news"));
        verify(logger, never()).info(eq("Successfully added news"));
    }

    @Test
    public void notAddedNewsAuthorIsNotNullTagsFailure() {
        News news = new News();
        news.setNewsId(1L);
        List<Tag> tags = new LinkedList<Tag>();
        tags.add(new Tag());
        when(newsRepository.add(news)).thenReturn(news);
        when(newsAuthorRepository.add(any(NewsAuthor.class))).thenReturn(true);
        when(newsTagRepository.add(any(NewsTag.class))).thenReturn(false);
        news = newsService.add(news, new Author(), tags);

        assertEquals((Long) 1L, news.getNewsId());
        verify(logger).error(eq("Failed to add tags to news"));
        verify(logger).error(eq("Failed to add news"));
        verify(logger, never()).info(eq("Successfully added tags to news"));
        verify(logger, never()).info(eq("Successfully added news"));
    }

    @Test
    public void found() {
        News news = new News();
        news.setNewsId(1L);
        when(newsRepository.find(1L)).thenReturn(news);
        news = newsService.find(1L);

        assertEquals((Long) 1L, news.getNewsId());
        verify(logger).info(eq("Successfully found news"));
    }

    @Test
    public void notFound() {
        when(newsRepository.find(1L)).thenReturn(null);
        News news = newsService.find(1L);

        assertNull(news);
        verify(logger).error(eq("Failed to find news"));
    }

    @Test
    public void updated() {
        News news = new News();
        news.setNewsId(1L);
        when(newsRepository.update(news)).thenReturn(true);
        boolean updated = newsService.update(news);

        assertTrue(updated);
        verify(logger).info(eq("Successfully updated news"));
    }

    @Test
    public void notUpdated() {
        when(newsRepository.update(null)).thenReturn(false);
        boolean updated = newsService.update(null);

        assertFalse(updated);
        verify(logger).error(eq("Failed to update news"));
    }

    @Test
    public void deleted() {
        News news = new News();
        news.setNewsId(1L);
        when(newsRepository.delete(news)).thenReturn(true);
        boolean deleted = newsService.delete(news);

        assertTrue(deleted);
        verify(logger).info(eq("Successfully deleted news"));
    }

    @Test
    public void notDeleted() {
        when(newsRepository.delete(null)).thenReturn(false);
        boolean deleted = newsService.delete(null);

        assertFalse(deleted);
        verify(logger).error(eq("Failed to delete news"));
    }

    @Test
    public void foundNewsBySearchCriteria() {
        when(newsRepository.search(any(String.class))).thenReturn(new LinkedList<News>());
        when(searchUtils.getSearchQuery(new SearchCriteria())).thenReturn("test");
        List<News> foundNews = newsService.search(new SearchCriteria());

        assertNotNull(foundNews);
        verify(logger).info(eq("Successfully retrieved news by search criteria"));
    }

    @Test
    public void notFoundNewsBySearchCriteria() {
        when(newsRepository.search(any(String.class))).thenReturn(null);
        when(searchUtils.getSearchQuery(any(SearchCriteria.class))).thenReturn("test");
        List<News> foundNews = newsService.search(new SearchCriteria());

        assertNull(foundNews);
        verify(logger).error(eq("Failed to find news by search criteria"));
    }

    @Test
    public void foundSortedNews() {
        when(newsRepository.findAllSorted()).thenReturn(new LinkedList<News>());
        List<News> sortedNews = newsService.findAllSorted();

        assertNotNull(sortedNews);
        verify(logger).info(eq("Successfully retrieved all news sorted by comments count"));
    }

    @Test
    public void notFoundSortedNews() {
        when(newsRepository.findAllSorted()).thenReturn(null);
        List<News> sortedNews = newsService.findAllSorted();

        assertNull(sortedNews);
        verify(logger).error(eq("Failed to retrieve all news sorted by comments count"));
    }

    @Test
    public void countedAllNews() {
        when(newsRepository.countAll()).thenReturn(1L);
        Long count = newsService.countAll();

        assertEquals((Long) 1L, count);
        verify(logger).info(eq("Successfully counted all news"));
    }

    @Test
    public void notCountedAllNews() {
        when(newsRepository.countAll()).thenReturn(-1L);
        Long count = newsService.countAll();

        assertEquals((Long) (-1L), count);
        verify(logger).error(eq("Failed to count all news"));
    }
}
