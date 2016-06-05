package com.epam.newsmanagement.command.news;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.command.Command;
import com.epam.newsmanagement.utils.InfoUtils;
import com.epam.newsmanagement.utils.JsonWriter;
import com.epam.newsmanagement.utils.NewsInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Previous news command. Replaces
 * current news viewed by user with
 * the next news according to the
 * search criteria.
 */
public class NextNewsCommand implements Command {
    private static final Logger logger = Logger.getLogger(NextNewsCommand.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private InfoUtils infoUtils;

    @Autowired
    private JsonWriter jsonWriter;


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Next news GET request");

        HttpSession session = request.getSession(false);
        SearchCriteria searchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
        Long newsRowNumber = (Long) session.getAttribute("newsRowNumber");
        newsRowNumber++;
        session.setAttribute("newsRowNumber", newsRowNumber);
        searchCriteria.setPageIndex(newsRowNumber);
        searchCriteria.setPageSize(1L);
        News news;
        try {
            news = newsService.search(searchCriteria).get(0);
        } catch (ServiceException e) {
            logger.error("Failed to execute next news command", e);
            response.sendRedirect("/news-client/404");
            return;
        }

        NewsInfo newsInfo;
        try {
            newsInfo = infoUtils.getNewsInfo(news, searchCriteria, newsRowNumber);
        } catch (ServiceException e) {
            logger.error("Failed to execute next news command", e);
            response.sendRedirect("/news-client/404");
            return;
        }
        jsonWriter.write(response, newsInfo);
    }
}
