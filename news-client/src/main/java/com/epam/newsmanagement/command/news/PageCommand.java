package com.epam.newsmanagement.command.news;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.command.Command;
import com.epam.newsmanagement.utils.InfoUtils;
import com.epam.newsmanagement.utils.JsonWriter;
import com.epam.newsmanagement.utils.NewsListInfo;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Page command. Responsible
 * for retrieving certain news
 * page satisfying provided
 * search criteria.
 */
public class PageCommand implements Command {
    private static final Logger logger = Logger.getLogger(FilterCommand.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private InfoUtils infoUtils;

    @Autowired
    private JsonWriter jsonWriter;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Page GET request");

        String searchCriteriaInString = request.getParameter("searchCriteria");
        SearchCriteria searchCriteria = objectMapper.readValue(searchCriteriaInString, SearchCriteria.class);
        if (searchCriteria.getPageIndex() == null) {
            Long pagesCount;
            try {
                pagesCount = newsService.countPagesBySearchCriteria(searchCriteria);
            } catch (ServiceException e) {
                logger.error("Failed to execute page command", e);
                response.sendRedirect("/news-client/404");
                return;
            }
            searchCriteria.setPageIndex(pagesCount);
        }
        HttpSession session = request.getSession(false);
        session.setAttribute("searchCriteria", searchCriteria);

        List<News> newsList;
        try {
            newsList = newsService.search(searchCriteria);
        } catch (ServiceException e) {
            logger.error("Failed to execute page command", e);
            response.sendRedirect("/news-client/404");
            return;
        }
        NewsListInfo newsListInfo;
        try {
            newsListInfo = infoUtils.getNewsListInfo(newsList, searchCriteria);
        } catch (ServiceException e) {
            logger.error("Failed to execute page command", e);
            response.sendRedirect("/news-client/404");
            return;
        }
        jsonWriter.write(response, newsListInfo);
    }
}
