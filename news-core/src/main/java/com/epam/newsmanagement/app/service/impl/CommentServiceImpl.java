package com.epam.newsmanagement.app.service.impl;

import com.epam.newsmanagement.app.dao.CommentRepository;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.CommentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Comment service implementation.
 */
public class CommentServiceImpl implements CommentService {
    private static final Logger logger = Logger.getLogger(CommentServiceImpl.class.getName());

    @Autowired
    private CommentRepository commentRepository;


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public Comment add(News news, Comment comment) throws ServiceException {
        logger.info("Adding new comment..");
        comment.setNews(news);
        comment.setCreationDate(new Timestamp(new java.util.Date().getTime()));
        Comment savedComment;
        try {
            savedComment = commentRepository.save(comment);
        } catch (DataAccessException e) {
            logger.error("Failed to add new comment");
            throw new ServiceException(e);
        }
        logger.info("Successfully added new comment");
        return savedComment;
    }


    @Override
    public Comment find(Long commentId) throws ServiceException {
        logger.info("Retrieving comment..");
        Comment comment;
        try {
            comment = commentRepository.findOne(commentId);
        } catch (DataAccessException e) {
            logger.error("Failed to find comment");
            throw new ServiceException(e);
        }
        logger.info("Successfully found comment");
        return comment;
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void update(Comment comment) throws ServiceException {
        logger.info("Updating comment..");
        try {
            commentRepository.save(comment);
        } catch (DataAccessException e) {
            logger.error("Failed to update comment");
            throw new ServiceException(e);
        }
        logger.info("Successfully updated comment");
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void delete(Comment comment) throws ServiceException {
        logger.info("Deleting comment..");
        try {
            Comment persistedCommit = commentRepository.findOne(comment.getCommentId());
            commentRepository.delete(persistedCommit);
        } catch (DataAccessException e) {
            logger.error("Failed to delete comment");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted comment");
    }


    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public void deleteAll(List<Comment> comments) throws ServiceException {
        logger.info("Deleting comments..");
        try {
            commentRepository.deleteAll(comments);
        } catch (DataAccessException e) {
            logger.error("Failed to delete comments");
            throw new ServiceException(e);
        }
        logger.info("Successfully deleted comments");
    }


    @Override
    public Long countAllByNews(News news) throws ServiceException {
        logger.info("Counting all news comments..");
        Long commentsCount;
        try {
            commentsCount = commentRepository.countAllByNews(news);
        } catch (DataAccessException e) {
            logger.error("Failed to count all news comments");
            throw new ServiceException(e);
        }
        logger.info("Successfully counted all news comments");
        return commentsCount;
    }


    @Override
    public List<Comment> findAllByNews(News news) throws ServiceException {
        logger.info("Retrieving all news comments..");
        List<Comment> commentsByNews;
        try {
            commentsByNews = commentRepository.findAllByNews(news);
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve all news comments");
            throw new ServiceException(e);
        }
        logger.info("Successfully retrieved all news comments");
        return commentsByNews;
    }
}
