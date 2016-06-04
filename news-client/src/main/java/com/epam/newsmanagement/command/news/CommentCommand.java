package com.epam.newsmanagement.command.news;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.CommentService;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.command.Command;
import com.epam.newsmanagement.utils.JsonWriter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Comment command. Adds new
 * comment to news.
 */
public class CommentCommand implements Command {
    private static final Logger logger = Logger.getLogger(CommentCommand.class);

    @Autowired
    private NewsService newsService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private JsonWriter jsonWriter;


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Comment POST request");

        HttpSession session = request.getSession(false);
        SearchCriteria searchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
        Long newsRowNumber = (Long) session.getAttribute("newsRowNumber");
        searchCriteria.setPageIndex(newsRowNumber);
        searchCriteria.setPageSize(1L);
        News news;
        try {
            news = newsService.search(searchCriteria).get(0);
        } catch (ServiceException e) {
            logger.error("Failed to execute comment command", e);
            return;
        }

        String commentText = request.getParameter("commentText");
        Comment comment = new Comment();
        comment.setCommentText(commentText);

        try {
            comment = commentService.add(news, comment);
        } catch (ServiceException e) {
            logger.error("Failed to execute comment command", e);
            return;
        }
        jsonWriter.write(response, comment);
    }
}
