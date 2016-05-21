package com.epam.newsmanagement.model;

import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;

import java.util.List;

/**
 * Contains all info needed to
 * render new news.
 */
public class NewsInfo {
    private News news;
    private List<Author> authors;
    private List<Comment> comments;
    private Boolean first;
    private Boolean last;

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }
}
