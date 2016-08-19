package com.epam.newsmanagement.utils;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.AuthorService;
import com.epam.newsmanagement.app.service.CommentService;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.service.TagService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Info builder. Retrieves certain
 * info from database and returns
 * this information encapsulated
 * in object.
 */
public class InfoUtils {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private TagService tagService;


    /**
     * Generates news list info
     * needed to render certain
     * news page.
     * @param newsList news list
     * which info is to be retrieved.
     * @param searchCriteria search criteria.
     * @return news list information.
     * @throws ServiceException
     */
    public NewsListInfo getNewsListInfo(List<News> newsList, SearchCriteria searchCriteria) throws ServiceException {
        NewsListInfo newsListInfo = new NewsListInfo();
        newsListInfo.setNewsList(newsList);

        Map<Long, List<Author>> authorsByNewsId = new HashMap<>();
        for (News news : newsList) {
            List<Author> authorsByNews = authorService.findAllByNews(news);
            authorsByNewsId.put(news.getNewsId(), authorsByNews);
        }
        newsListInfo.setAuthorsByNewsId(authorsByNewsId);

        Map<Long, List<Tag>> tagsByNewsId = new HashMap<>();
        for (News news : newsList) {
            List<Tag> tagsByNews = tagService.findAllByNews(news);
            tagsByNewsId.put(news.getNewsId(), tagsByNews);
        }
        newsListInfo.setTagsByNewsId(tagsByNewsId);

        Map<Long, Long> commentsCountByNewsId = new HashMap<>();
        for (News news : newsList) {
            Long newsCommentsCount = commentService.countAllByNews(news);
            commentsCountByNewsId.put(news.getNewsId(), newsCommentsCount);
        }
        newsListInfo.setCommentsCountByNewsId(commentsCountByNewsId);

        Long pagesCount = newsService.countPagesBySearchCriteria(searchCriteria);
        newsListInfo.setPagesCount(pagesCount);

        return newsListInfo;
    }


    /**
     * Generates news info
     * needed to render certain news.
     * @param newsId id of the news which
     * info is to be retrieved.
     * @param searchCriteria search criteria.
     * @return news information.
     * @throws ServiceException
     */
    public NewsInfo getNewsInfo(Long newsId, SearchCriteria searchCriteria) throws ServiceException {
        NewsInfo newsInfo = new NewsInfo();

        News news = newsService.find(newsId);
        newsInfo.setNews(news);

        List<Author> authors = authorService.findAllByNews(news);
        newsInfo.setAuthors(authors);

        List<Comment> comments = commentService.findAllByNews(news);
        newsInfo.setComments(comments);

        Long newsRowNumber = newsService.rowNumberBySearchCriteria(searchCriteria, news);

        searchCriteria.setPageSize(1L);
        Long newsCount = newsService.countPagesBySearchCriteria(searchCriteria);
        if (newsRowNumber != 1) {
            searchCriteria.setPageIndex(newsRowNumber - 1);
            Long previousId = newsService.search(searchCriteria).get(0).getNewsId();
            newsInfo.setPreviousId(previousId);
        }
        if (!newsRowNumber.equals(newsCount)) {
            searchCriteria.setPageIndex(newsRowNumber + 1);
            Long nextId = newsService.search(searchCriteria).get(0).getNewsId();
            newsInfo.setNextId(nextId);
        }

        return newsInfo;
    }


    /**
     * Constructs news edit info for
     * certain news by id.
     * @param newsId specifies news.
     * @return new edit info.
     * @throws ServiceException
     */
    public NewsEditInfo getNewsEditInfo(Long newsId) throws ServiceException {
        NewsEditInfo newsEditInfo = new NewsEditInfo();

        News news = newsService.find(newsId);
        newsEditInfo.setNews(news);

        List<Author> authors = authorService.findAllByNews(news);
        newsEditInfo.setAuthors(authors);

        List<Tag> tags = tagService.findAllByNews(news);
        newsEditInfo.setTags(tags);

        return newsEditInfo;
    }


    /**
     * Retrieves all authors and tags.
     * @return all authors and tags.
     * @throws ServiceException
     */
    public AuthorsAndTagsInfo getAuthorsAndTagsInfo() throws ServiceException {
        AuthorsAndTagsInfo authorsAndTagsInfo = new AuthorsAndTagsInfo();

        List<Author> notExpiredAuthors = authorService.findNotExpired();
        authorsAndTagsInfo.setNotExpiredAuthors(notExpiredAuthors);

        List<Tag> tags = tagService.findAll();
        authorsAndTagsInfo.setTags(tags);

        return authorsAndTagsInfo;
    }
}
