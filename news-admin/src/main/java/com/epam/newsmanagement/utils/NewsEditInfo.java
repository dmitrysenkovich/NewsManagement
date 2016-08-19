package com.epam.newsmanagement.utils;

import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;

import java.util.List;

/**
 * Provides info for news edit page.
 */
public class NewsEditInfo {
    private News news;
    private List<Author> authors;
    private List<Tag> tags;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
