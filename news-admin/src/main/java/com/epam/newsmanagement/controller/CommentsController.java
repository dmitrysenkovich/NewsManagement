package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.CommentService;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Comments controller. Responsible
 * for managing comments.
 */
@Controller
public class CommentsController {
    private static final Logger logger = Logger.getLogger(CommentsController.class);

    @Autowired
    private NewsService newsService;
    @Autowired
    private CommentService commentService;


    /**
     * Adds new comment to the news.
     * @param commentText new comment
     * text
     * @param request request.
     * @return new comment.
     * @throws ServiceException
     */
    @RequestMapping(value = "/comment/add", method = RequestMethod.PUT)
    @ResponseBody
    public Comment add(@RequestBody String commentText, HttpServletRequest request) throws ServiceException {
        logger.info("Add PUT request");

        HttpSession session = request.getSession(false);
        SearchCriteria searchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
        Long newsRowNumber = (Long) session.getAttribute("newsRowNumber");
        searchCriteria.setPageIndex(newsRowNumber);
        searchCriteria.setPageSize(1L);
        News news = newsService.search(searchCriteria).get(0);

        Comment comment = new Comment();
        comment.setCommentText(commentText);

        comment = commentService.add(news, comment);

        return comment;
    }


    /**
     * Deletes news comment.
     * @param commentId specifies comment
     * that is to be deleted.
     * @throws ServiceException
     */
    @RequestMapping(value = "/comment/delete/{commentId}", method = RequestMethod.POST)
    @ResponseBody
    public void delete(@PathVariable Long commentId) throws ServiceException {
        logger.info("Delete POST request");

        Comment comment = new Comment();
        comment.setCommentId(commentId);
        commentService.delete(comment);
    }
}
