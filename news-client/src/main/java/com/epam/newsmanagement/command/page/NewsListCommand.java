package com.epam.newsmanagement.command.page;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.AuthorService;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.service.TagService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.command.Command;
import com.epam.newsmanagement.utils.InfoUtils;
import com.epam.newsmanagement.utils.NewsListInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * News list command. Dispatches requests
 * to first page of news list.
 */
public class NewsListCommand implements Command {
    private static final Logger logger = Logger.getLogger(NewsListCommand.class);

    @Autowired
    private AuthorService authorService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private TagService tagService;

    @Autowired
    private InfoUtils infoBuilder;


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("News list GET request");

        List<Author> notExpiredAuthors;
        try {
            notExpiredAuthors = authorService.getNotExpired();
            request.setAttribute("notExpiredAuthors", notExpiredAuthors);
        } catch (ServiceException e) {
            logger.error("Failed to generate news list page", e);
            response.sendRedirect("/news-client/500");
            return;
        }

        List<Tag> tags;
        try {
            tags = tagService.getAll();
            request.setAttribute("tags", tags);
        } catch (ServiceException e) {
            logger.error("Failed to generate news list page", e);
            response.sendRedirect("/news-client/500");
            return;
        }

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPageIndex(1L);
        List<News> newsList;
        try {
            newsList = newsService.search(searchCriteria);
            request.setAttribute("newsList", newsList);
        } catch (ServiceException e) {
            logger.error("Failed to generate news list page", e);
            response.sendRedirect("/news-client/500");
            return;
        }

        NewsListInfo newsListInfo;
        try {
            newsListInfo = infoBuilder.getNewsListInfo(newsList, searchCriteria);
            request.setAttribute("authorsByNewsId", newsListInfo.getAuthorsByNewsId());
            request.setAttribute("tagsByNewsId", newsListInfo.getTagsByNewsId());
            request.setAttribute("commentsCountByNewsId", newsListInfo.getCommentsCountByNewsId());
            request.setAttribute("pagesCount", newsListInfo.getPagesCount());
        } catch (ServiceException e) {
            logger.error("Failed to generate news list page", e);
            response.sendRedirect("/news-client/500");
            return;
        }

        request.getRequestDispatcher("/news-list.tiles").forward(request, response);
    }
}
