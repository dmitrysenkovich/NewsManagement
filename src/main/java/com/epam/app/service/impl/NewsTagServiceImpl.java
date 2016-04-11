package com.epam.app.service.impl;

import com.epam.app.dao.NewsTagRepository;
import com.epam.app.model.Tag;
import com.epam.app.model.News;
import com.epam.app.model.NewsTag;
import com.epam.app.service.NewsTagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * NewsTag service implementation.
 */
public class NewsTagServiceImpl implements NewsTagService {
    private static Logger logger = Logger.getLogger(NewsTagServiceImpl.class.getName());

    @Autowired
    private NewsTagRepository newsTagRepository;


    @Transactional
    public boolean add(News news, Tag tag) {
        logger.info("Adding new tag to news..");
        NewsTag newsTag = new NewsTag();
        newsTag.setNewsId(news.getNewsId());
        newsTag.setTagId(tag.getTagId());
        boolean added = newsTagRepository.add(newsTag);
        if (added)
            logger.info("Successfully added new tag to news");
        else
            logger.error("Failed to add tag to news");
        return added;
    }


    @Transactional
    public boolean delete(News news, Tag tag) {
        logger.info("Deleting tag from news..");
        NewsTag newsTag = new NewsTag();
        newsTag.setNewsId(news.getNewsId());
        newsTag.setTagId(tag.getTagId());
        boolean deleted = newsTagRepository.delete(newsTag);
        if (deleted)
            logger.info("Successfully deleted tag from news");
        else
            logger.error("Failed to delete tag from news");
        return deleted;
    }
}
