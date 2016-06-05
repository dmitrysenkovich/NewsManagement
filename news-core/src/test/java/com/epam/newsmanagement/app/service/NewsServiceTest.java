package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.dao.NewsAuthorRepository;
import com.epam.newsmanagement.app.dao.NewsRepository;
import com.epam.newsmanagement.app.dao.NewsTagRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.NewsAuthor;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.impl.NewsServiceImpl;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.app.utils.SearchUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
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
    public void notAddedNewsAuthorsAreNullTagsAreNull() throws Exception {
        News news = new News();
        when(newsRepository.add(news)).thenReturn(1L);
        catchException(() -> newsService.add(news, null, null));
        assert caughtException() instanceof ServiceException;

        assertEquals((Long) 1L, news.getNewsId());
        verify(newsRepository, times(1)).add(news);
        verify(newsAuthorRepository, times(0)).addAll(any(News.class), any(List.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void addedNewsAuthorsAreNotNullTagsAreNull() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        List<Author> authors = new LinkedList<>();
        authors.add(new Author());
        when(newsRepository.add(news)).thenReturn(1L);
        doNothing().when(newsAuthorRepository).add(any(NewsAuthor.class));
        news = newsService.add(news, authors, null);

        assertEquals((Long) 1L, news.getNewsId());
        verify(newsRepository, times(1)).add(news);
        verify(newsAuthorRepository, times(1)).addAll(any(News.class), any(List.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void didNotAddNewsAuthorsAreNullTagsAreNotNull() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        when(newsRepository.add(news)).thenReturn(1L);
        catchException(() -> newsService.add(news, null, tags));
        assert caughtException() instanceof ServiceException;

        assertEquals((Long) 1L, news.getNewsId());
        verify(newsRepository, times(1)).add(news);
        verify(newsAuthorRepository, times(0)).addAll(any(News.class), any(List.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void addedNewsAuthorsAreNotNullTagsAreNotNull() throws Exception {
        News news = new News();
        news.setNewsId(1L);
        List<Author> authors = new LinkedList<>();
        authors.add(new Author());
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        when(newsRepository.add(news)).thenReturn(1L);
        doNothing().when(newsAuthorRepository).add(any(NewsAuthor.class));
        doNothing().when(newsTagRepository).addAll(any(News.class), any(List.class));
        news = newsService.add(news, authors, tags);

        assertEquals((Long) 1L, news.getNewsId());
        verify(newsRepository, times(1)).add(news);
        verify(newsAuthorRepository, times(1)).addAll(any(News.class), any(List.class));
        verify(newsTagRepository, times(1)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void didNotAddNewsAuthorsAreNullTagsAreNullNewsFailure() throws Exception {
        doThrow(new DaoException()).when(newsRepository).add(any(News.class));
        catchException(() -> newsService.add(new News(), null, null));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).addAll(any(News.class), any(List.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void didNotAddNewsAuthorsAreNotNullTagsAreNullNewsFailure() throws Exception {
        List<Author> authors = new LinkedList<>();
        authors.add(new Author());
        doThrow(new DaoException()).when(newsRepository).add(any(News.class));
        catchException(() -> newsService.add(new News(), authors, null));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).addAll(any(News.class), any(List.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void didNotAddNewsAuthorsAreNullTagsAreNotNullNewsFailure() throws Exception {
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        doThrow(new DaoException()).when(newsRepository).add(any(News.class));
        catchException(() -> newsService.add(new News(), null, tags));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).addAll(any(News.class), any(List.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void didNotAddNewsAuthorsAreNotNullTagsAreNotNullNewsFailure() throws Exception {
        List<Author> authors = new LinkedList<>();
        authors.add(new Author());
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        doThrow(new DaoException()).when(newsRepository).add(any(News.class));
        catchException(() -> newsService.add(new News(), authors, tags));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).addAll(any(News.class), any(List.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void didNotAddNewsTagsAreNullAuthorsAreNull() throws Exception {
        when(newsRepository.add(any(News.class))).thenReturn(1L);
        doThrow(new DaoException()).when(newsAuthorRepository).add(any(NewsAuthor.class));
        catchException(() -> newsService.add(new News(), null, null));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).addAll(any(News.class), any(List.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void didNotAddNewsTagsAreNotNullAuthorsAreEmpty() throws Exception {
        List<Author> authors = new LinkedList<>();
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        when(newsRepository.add(any(News.class))).thenReturn(1L);
        doThrow(new DaoException()).when(newsAuthorRepository).add(any(NewsAuthor.class));
        catchException(() -> newsService.add(new News(), authors, tags));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).addAll(any(News.class), any(List.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void didNotAddNewsAuthorsAreNullTagsFailure() throws Exception {
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        when(newsRepository.add(any(News.class))).thenReturn(1L);
        doNothing().when(newsAuthorRepository).add(any(NewsAuthor.class));
        doThrow(new DaoException()).when(newsTagRepository).addAll(any(News.class), any(List.class));
        catchException(() -> newsService.add(new News(), null, tags));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(0)).addAll(any(News.class), any(List.class));
        verify(newsTagRepository, times(0)).addAll(any(News.class), any(List.class));
    }


    @Test
    public void didNotAddNewsAuthorsAreNotNullTagsFailure() throws Exception {
        List<Author> authors = new LinkedList<>();
        authors.add(new Author());
        List<Tag> tags = new LinkedList<>();
        tags.add(new Tag());
        when(newsRepository.add(any(News.class))).thenReturn(1L);
        doNothing().when(newsAuthorRepository).add(any(NewsAuthor.class));
        doThrow(new DaoException()).when(newsTagRepository).addAll(any(News.class), any(List.class));
        catchException(() -> newsService.add(new News(), authors, tags));
        assert caughtException() instanceof ServiceException;

        verify(newsRepository, times(1)).add(any(News.class));
        verify(newsAuthorRepository, times(1)).addAll(any(News.class), any(List.class));
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
    public void didNotFind() throws Exception {
        doThrow(new DaoException()).when(newsRepository).find(any(Long.class));
        newsService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        doNothing().when(newsRepository).update(any(News.class));
        newsService.update(new News());
    }


    @Test(expected = ServiceException.class)
    public void didNotUpdate() throws Exception {
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
    public void didNotDelete() throws Exception {
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
    public void didNotFindNewsBySearchCriteria() throws Exception {
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
    public void didNotFindSortedNews() throws Exception {
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
    public void didNotCountAllNews() throws Exception {
        doThrow(new DaoException()).when(newsRepository).countAll();
        newsService.countAll();
    }


    @Test
    public void countedNewsBySearchCriteria() throws Exception {
        when(newsRepository.countPagesBySearchCriteria(any(String.class))).thenReturn(1L);
        when(searchUtils.getSearchQuery(any(SearchCriteria.class))).thenReturn(new String());
        Long count = newsService.countPagesBySearchCriteria(new SearchCriteria());

        assertNotNull(count);
    }


    @Test(expected = ServiceException.class)
    public void didNotCountNewsBySearchCriteria() throws Exception {
        doThrow(new DaoException()).when(newsRepository).countPagesBySearchCriteria(any(String.class));
        when(searchUtils.getSearchQuery(any(SearchCriteria.class))).thenReturn(new String());
        newsService.countPagesBySearchCriteria(new SearchCriteria());
    }


    @Test
    public void deletedAllNews() throws Exception {
        doNothing().when(newsRepository).deleteAll(any(List.class));
        newsService.deleteAll(new ArrayList<>());
    }


    @Test(expected = ServiceException.class)
    public void didNotDeleteAllNews() throws Exception {
        doThrow(new DaoException()).when(newsRepository).deleteAll(any(List.class));
        newsService.deleteAll(new ArrayList<>());
    }


    @Test
    public void retrievedNewRowNumberBySearchCriteria() throws Exception {
        when(newsRepository.rowNumberBySearchCriteria(any(String.class))).thenReturn(1L);
        when(searchUtils.getSearchQuery(any(SearchCriteria.class))).thenReturn(new String());
        Long rowNumber = newsService.rowNumberBySearchCriteria(new SearchCriteria(), new News());

        assertEquals((Long) 1L, rowNumber);
    }


    @Test(expected = ServiceException.class)
    public void didNotRetrieveNewRowNumberBySearchCriteria() throws Exception {
        doThrow(new DaoException()).when(newsRepository).rowNumberBySearchCriteria(any(String.class));
        when(searchUtils.getSearchQuery(any(SearchCriteria.class))).thenReturn(new String());
        newsService.rowNumberBySearchCriteria(new SearchCriteria(), new News());
    }
}
