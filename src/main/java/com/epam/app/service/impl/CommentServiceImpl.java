package com.epam.app.service.impl;

import com.epam.app.dao.CommentRepository;
import com.epam.app.model.Comment;
import com.epam.app.model.News;
import com.epam.app.service.CommentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Comment service implementation.
 */
public class CommentServiceImpl implements CommentService {
    private static Logger logger = Logger.getLogger(CommentServiceImpl.class.getName());

    @Autowired
    private CommentRepository commentRepository;


    @Override
    @Transactional
    public Comment add(News news, Comment comment) {
        logger.info("Adding new comment..");
        comment.setNewsId(news.getNewsId());
        comment.setCreationDate(new Timestamp(new java.util.Date().getTime()));
        comment = commentRepository.add(comment);
        if (comment.getCommentId() != null)
            logger.info("Successfully added new comment");
        else
            logger.error("Failed to add new comment");
        return comment;
    }


    @Override
    public Comment find(Long commentId) {
        logger.info("Retrieving comment..");
        Comment comment = commentRepository.find(commentId);
        if (comment != null)
            logger.info("Successfully found comment");
        else
            logger.error("Failed to find comment");
        return comment;
    }


    @Override
    @Transactional
    public boolean update(Comment comment) {
        logger.info("Updating comment..");
        boolean updated = commentRepository.update(comment);
        if (updated)
            logger.info("Successfully updated comment");
        else
            logger.error("Failed to update comment");
        return updated;
    }


    @Override
    @Transactional
    public boolean delete(Comment comment) {
        logger.info("Deleting comment..");
        boolean deleted = commentRepository.delete(comment);
        if (deleted)
            logger.info("Successfully deleted comment");
        else
            logger.error("Failed to delete comment");
        return deleted;
    }


    @Override
    @Transactional
    public List<Comment> addAll(News news, List<Comment> comments) {
        logger.info("Adding comments..");
        for (Comment comment : comments) {
            comment.setNewsId(news.getNewsId());
            comment.setCreationDate(new Timestamp(new java.util.Date().getTime()));
        }
        comments = commentRepository.addAll(comments);
        boolean allAdded = true;
        for (Comment comment : comments)
            if (comment.getCommentId() == null)
                allAdded = false;
        if (allAdded)
            logger.info("Successfully added comments");
        else
            logger.error("Failed to add comments");
        return comments;
    }


    @Override
    @Transactional
    public boolean deleteAll(List<Comment> comments) {
        logger.info("Deleting comments..");
        boolean deleted = commentRepository.deleteAll(comments);
        if (deleted)
            logger.info("Successfully deleted comments");
        else
            logger.error("Failed to delete comments");
        return deleted;
    }
}
