package com.epam.app.service;

import com.epam.app.dao.NewsAuthorRepository;
import com.epam.app.dao.NewsRepository;
import com.epam.app.dao.NewsTagRepository;
import com.epam.app.exception.DaoException;
import com.epam.app.exception.ServiceException;
import com.epam.app.model.Author;
import com.epam.app.model.News;
import com.epam.app.model.NewsAuthor;
import com.epam.app.model.NewsTag;
import com.epam.app.model.Tag;
import com.epam.app.service.impl.NewsServiceImpl;
import com.epam.app.utils.SearchCriteria;
import com.epam.app.utils.SearchUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedList;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
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

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void addedNewsAuthorIsNullTagsAreNull() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        when(newsRepository.add(news)).thenReturn(1L);
        news = newsService.add(news, null, null);

        assertEquals((Long) 1L, news.getNewsId());
        verify(newsRepository, times(1)).add(news);
        verify(newsAuthorRepository, times(0)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void addedNewsAuthorIsNotNullTagsAreNull() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        when(newsRepository.add(news)).thenReturn(1L);
        doNothing().when(newsAuthorRepository).add(any(NewsAuthor.class));
        news = newsService.add(news, new Author(), null);

        assertEquals((Long) 1L, news.getNewsId());
        verify(newsRepository, times(1)).add(news);
        verify(newsAuthorRepository, times(1)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void addedNewsAuthorIsNullTagsAreNotNull() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        when(newsRepository.add(news)).thenReturn(1L);
        doNothing().when(newsTagRepository).addAll(any(News.class), any(List.class));
        news = newsService.add(news, null, tags);

        assertEquals((Long) 1L, news.getNewsId());
        verify(newsRepository, times(1)).add(news);
        verify(newsAuthorRepository, times(0)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(1)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void addedNewsAuthorIsNotNullTagsAreNotNull() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        when(newsRepository.add(news)).thenReturn(1L);
        doNothing().when(newsAuthorRepository).add(any(NewsAuthor.class));
        doNothing().when(newsTagRepository).addAll(any(News.class), any(List.class));
        news = newsService.add(news, new Author(), tags);

        assertEquals((Long) 1L, news.getNewsId());
        verify(newsRepository, times(1)).add(news);
        verify(newsAuthorRepository, times(1)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(1)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void notAddedNewsAuthorIsNullTagsAreNullNewsFailure() throws Exception {
        doThrow(new DaoException()).when(newsRepository).add(any(News.class));
        catchException(() -> newsService.add(new News(), null, null));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void notAddedNewsAuthorIsNotNullTagsAreNullNewsFailure() throws Exception {
        doThrow(new DaoException()).when(newsRepository).add(any(News.class));
        catchException(() -> newsService.add(new News(), new Author(), null));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void notAddedNewsAuthorIsNullTagsAreNotNullNewsFailure() throws Exception {
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        doThrow(new DaoException()).when(newsRepository).add(any(News.class));
        catchException(() -> newsService.add(new News(), null, tags));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void notAddedNewsAuthorIsNotNullTagsAreNotNullNewsFailure() throws Exception {
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        doThrow(new DaoException()).when(newsRepository).add(any(News.class));
        catchException(() -> newsService.add(new News(), new Author(), tags));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void notAddedNewsTagsAreNullAuthorFailure() throws Exception {
        when(newsRepository.add(any(News.class))).thenReturn(1L);
        doThrow(new DaoException()).when(newsAuthorRepository).add(any(NewsAuthor.class));
        catchException(() -> newsService.add(new News(), new Author(), null));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(1)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void notAddedNewsTagsAreNotNullAuthorFailure() throws Exception {
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        when(newsRepository.add(any(News.class))).thenReturn(1L);
        doThrow(new DaoException()).when(newsAuthorRepository).add(any(NewsAuthor.class));
        catchException(() -> newsService.add(new News(), new Author(), tags));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(1)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void notAddedNewsAuthorIsNullTagsFailure() throws Exception {
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        when(newsRepository.add(any(News.class))).thenReturn(1L);
        doNothing().when(newsAuthorRepository).add(any(NewsAuthor.class));
        doThrow(new DaoException()).when(newsTagRepository).addAll(any(News.class), any(List.class));
        catchException(() -> newsService.add(new News(), null, tags));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(1)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void notAddedNewsAuthorIsNotNullTagsFailure() throws Exception {
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        when(newsRepository.add(any(News.class))).thenReturn(1L);
        doNothing().when(newsAuthorRepository).add(any(NewsAuthor.class));
        doThrow(new DaoException()).when(newsTagRepository).addAll(any(News.class), any(List.class));
        catchException(() -> newsService.add(new News(), new Author(), tags));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(1)).add(any(NewsAuthor.class));
        verify(newsTagRepository, times(1)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void found() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        when(newsRepository.find(1L)).thenReturn(news);
        news = newsService.find(1L);

        assertEquals((Long) 1L, news.getNewsId());
    }


    @Test(expected = ServiceException.class)
    public void notFound() throws Exception {
        doThrow(new DaoException()).when(newsRepository).find(any(Long.class));
        newsService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        doNothing().when(newsRepository).update(any(News.class));
        newsService.update(new News());
    }


    @Test(expected = ServiceException.class)
    public void notUpdated() throws Exception {
        doThrow(new DaoException()).when(newsRepository).update(any(News.class));
        newsService.update(new News());
    }


    @Test
    public void deleted() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        doNothing().when(newsRepository).delete(any(News.class));
        newsService.delete(news);
    }


    @Test(expected = ServiceException.class)
    public void notDeleted() throws Exception {
        doThrow(new DaoException()).when(newsRepository).delete(any(News.class));
        newsService.delete(new News());
    }


    @Test
    public void foundNewsBySearchCriteria() throws Exception {
        when(newsRepository.search(any(String.class))).thenReturn(new LinkedList<>());
        when(searchUtils.getSearchQuery(any(SearchCriteria.class))).thenReturn(new String());
        List<News> foundNews = newsService.search(new SearchCriteria());

        assertNotNull(foundNews);
    }


    @Test(expected = ServiceException.class)
    public void notFoundNewsBySearchCriteria() throws Exception {
        doThrow(new DaoException()).when(newsRepository).search(any(String.class));
        when(searchUtils.getSearchQuery(any(SearchCriteria.class))).thenReturn(new String());
        newsService.search(new SearchCriteria());
    }


    @Test
    public void foundSortedNews() throws Exception {
        when(newsRepository.findAllSorted()).thenReturn(new LinkedList<>());
        List<News> sortedNews = newsService.findAllSorted();

        assertNotNull(sortedNews);
    }


    @Test(expected = ServiceException.class)
    public void notFoundSortedNews() throws Exception {
        doThrow(new DaoException()).when(newsRepository).findAllSorted();
        newsService.findAllSorted();
    }


    @Test
    public void countedAllNews() throws Exception {
        when(newsRepository.countAll()).thenReturn(1L);
        Long count = newsService.countAll();

        assertEquals((Long) 1L, count);
    }


    @Test(expected = ServiceException.class)
    public void notCountedAllNews() throws Exception {
        doThrow(new DaoException()).when(newsRepository).countAll();
        newsService.countAll();
    }
}
