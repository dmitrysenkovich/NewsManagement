package com.epam.newsmanagement.command.news;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.command.Command;
import com.epam.newsmanagement.utils.InfoUtils;
import com.epam.newsmanagement.utils.NewsInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * News command. Returns page
 * with specified news id.
 */
public class NewsCommand implements Command {
    private static final Logger logger = Logger.getLogger(NewsCommand.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private InfoUtils infoUtils;


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        int idIndex = uri.lastIndexOf("/");
        String newsIdInString = uri.substring(idIndex + 1);
        Long newsId = Long.parseLong(newsIdInString);
        News news;
        try {
            news = newsService.find(newsId);
        } catch (ServiceException e) {
            logger.error("Failed to execute news command", e);
            return;
        }

        HttpSession session = request.getSession(true);
        SearchCriteria searchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
        if (searchCriteria == null)
            searchCriteria = new SearchCriteria();
        session.setAttribute("searchCriteria", searchCriteria);

        Long newsRowNumber;
        try {
            newsRowNumber = newsService.rowNumberBySearchCriteria(searchCriteria, news);
        } catch (ServiceException e) {
            logger.error("Failed to execute news command", e);
            return;
        }
        session.setAttribute("newsRowNumber", newsRowNumber);

        NewsInfo newsInfo;
        try {
            newsInfo = infoUtils.getNewsInfo(news, searchCriteria, newsRowNumber);
        } catch (ServiceException e) {
            logger.error("Failed to execute news command", e);
            return;
        }
        request.setAttribute("news", news);
        request.setAttribute("authors", newsInfo.getAuthors());
        request.setAttribute("comments", newsInfo.getComments());

        if (newsInfo.getFirst())
            request.setAttribute("first", true);
        if (newsInfo.getLast())
            request.setAttribute("last", true);

        request.getRequestDispatcher("/news.tiles").forward(request, response);
    }
}
