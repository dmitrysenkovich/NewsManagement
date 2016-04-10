package com.epam.app.service.impl;

import com.epam.app.dao.NewsAuthorRepository;
import com.epam.app.dao.NewsRepository;
import com.epam.app.dao.NewsTagRepository;
import com.epam.app.model.*;
import com.epam.app.service.NewsService;
import com.epam.app.utils.SearchCriteria;
import com.epam.app.utils.SearchUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * News service implementation.
 */
public class NewsServiceImpl implements NewsService {
    private static Logger logger = Logger.getLogger(NewsServiceImpl.class.getName());

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private NewsAuthorRepository newsAuthorRepository;
    @Autowired
    private NewsTagRepository newsTagRepository;

    @Autowired
    private SearchUtils searchUtils;


    public News add(News news, Author author, List<Tag> tags) {
        logger.info("Adding news..");
        news.setCreationDate(new Timestamp(new java.util.Date().getTime()));
        news.setModificationDate(new Date(new java.util.Date().getTime()));
        news = newsRepository.add(news);
        if (news.getNewsId() == null) {
            logger.error("Failed to add news");
            return news;
        }

        boolean addedNewsAuthorRelation = true;
        if (author != null) {
            logger.info("Adding author to news..");
            NewsAuthor newsAuthor = new NewsAuthor();
            newsAuthor.setNewsId(news.getNewsId());
            newsAuthor.setAuthorId(author.getAuthorId());
            addedNewsAuthorRelation = newsAuthorRepository.add(newsAuthor);
            if (!addedNewsAuthorRelation) {
                logger.error("Failed to add author to news");
                logger.error("Failed to add news");
                return news;
            }
            logger.info("Successfully added author to news");
        }

        boolean addedAllNewsTagRelations = true;
        if (tags != null) {
            logger.info("Adding tags to news..");
            for (Tag tag : tags) {
                NewsTag newsTag = new NewsTag();
                newsTag.setNewsId(news.getNewsId());
                newsTag.setTagId(tag.getTagId());
                addedAllNewsTagRelations = newsTagRepository.add(newsTag);
                if (!addedAllNewsTagRelations) {
                    logger.error("Failed to add tags to news");
                    break;
                }
            }
            if (addedAllNewsTagRelations)
                logger.info("Successfully added tags to news");
        }
        if (addedNewsAuthorRelation && addedAllNewsTagRelations)
            logger.info("Successfully added news");
        else
            logger.error("Failed to add news");

        return news;
    }


    public News find(Long newsId) {
        logger.info("Retrieving news..");
        News news = newsRepository.find(newsId);
        if (news != null)
            logger.info("Successfully found news");
        else
            logger.error("Failed to find news");
        return news;
    }


    public boolean update(News news) {
        logger.info("Updating news..");
        boolean updated = newsRepository.update(news);
        if (updated)
            logger.info("Successfully updated news");
        else
            logger.error("Failed to update news");
        return updated;
    }


    public boolean delete(News news) {
        logger.info("Deleting news..");
        boolean deleted = newsRepository.delete(news);
        if (deleted)
            logger.info("Successfully deleted news");
        else
            logger.error("Failed to delete news");
        return deleted;
    }


    public List<News> search(SearchCriteria searchCriteria) {
        logger.info("Searching certain news..");
        final String SEARCH_CRITERIA_QUERY = searchUtils.getSearchQuery(searchCriteria);
        List<News> fitNews = newsRepository.search(SEARCH_CRITERIA_QUERY);
        if (fitNews != null)
            logger.info("Successfully retrieved news ny search criteria");
        else
            logger.error("Failed to find news by search criteria");
        return fitNews;
    }


    public List<News> findAllSorted() {
        logger.info("Retrieving all news sorted by comments count..");
        List<News> sortedNews = newsRepository.findAllSorted();
        if (sortedNews != null)
            logger.info("Successfully retrieved all news sorted by comments count");
        else
            logger.error("Failed to retrieve all news sorted by comments count");
        return sortedNews;
    }


    public Long countAll() {
        logger.info("Counting all news..");
        Long newsCount = newsRepository.countAll();
        if (newsCount != -1)
            logger.info("Successfully counted all news");
        else
            logger.error("Failed to count all news");
        return newsCount;
    }
}
