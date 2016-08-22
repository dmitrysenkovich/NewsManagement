package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.CommentService;
import com.epam.newsmanagement.app.service.NewsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

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

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * Adds new comment to the news.
     * @param requestBody contains current
     * news id and new comment text in JSON.
     * @return new comment.
     * @throws ServiceException
     */
    @RequestMapping(value = "/comment/add", method = RequestMethod.PUT)
    @ResponseBody
    public Comment add(@RequestBody String requestBody) throws ServiceException, IOException {
        logger.info("Add PUT request");

        JsonNode jsonNode = objectMapper.readTree(requestBody);
        Long newsId = objectMapper.convertValue(jsonNode.get("newsId"), Long.class);
        String commentText = objectMapper.convertValue(jsonNode.get("commentText"), String.class);

        News news = newsService.find(newsId);

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
