package com.epam.newsmanagement.model;

import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;

import java.util.List;
import java.util.Map;

/**
 * Contains all info needed to
 * render new news list.
 */
public class NewsListInfo {
    private List<News> newsList;
    private Map<Long, List<Author>> authorsByNewsId;
    private Map<Long, List<Tag>> tagsByNewsId;
    private Map<Long, Long> commentsCountByNewsId;

    private Long pagesCount;

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public Map<Long, List<Author>> getAuthorsByNewsId() {
        return authorsByNewsId;
    }

    public void setAuthorsByNewsId(Map<Long, List<Author>> authorsByNewsId) {
        this.authorsByNewsId = authorsByNewsId;
    }

    public Map<Long, List<Tag>> getTagsByNewsId() {
        return tagsByNewsId;
    }

    public void setTagsByNewsId(Map<Long, List<Tag>> tagsByNewsId) {
        this.tagsByNewsId = tagsByNewsId;
    }

    public Map<Long, Long> getCommentsCountByNewsId() {
        return commentsCountByNewsId;
    }

    public void setCommentsCountByNewsId(Map<Long, Long> commentsCountByNewsId) {
        this.commentsCountByNewsId = commentsCountByNewsId;
    }

    public Long getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(Long pagesCount) {
        this.pagesCount = pagesCount;
    }
}
