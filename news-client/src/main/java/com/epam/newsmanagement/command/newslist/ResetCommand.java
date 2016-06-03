package com.epam.newsmanagement.command.newslist;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.command.Command;
import com.epam.newsmanagement.utils.JsonWriter;
import com.epam.newsmanagement.utils.InfoUtils;
import com.epam.newsmanagement.utils.NewsListInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Reset command. Resets
 * news list to default state.
 */
public class ResetCommand implements Command {
    private static final Logger logger = Logger.getLogger(ResetCommand.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private InfoUtils infoUtils;

    @Autowired
    private JsonWriter jsonWriter;


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Reset GET request");

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPageIndex(1L);
        HttpSession session = request.getSession(false);
        session.setAttribute("searchCriteria", searchCriteria);

        List<News> newsList = null;
        try {
            newsList = newsService.search(searchCriteria);
        } catch (ServiceException e) {
            logger.error("Failed to execute reset command", e);
            return;
        }
        NewsListInfo newsListInfo = null;
        try {
            newsListInfo = infoUtils.getNewsListInfo(newsList, searchCriteria);
        } catch (ServiceException e) {
            logger.error("Failed to execute reset command", e);
            return;
        }
        jsonWriter.write(response, newsListInfo);
    }
}
