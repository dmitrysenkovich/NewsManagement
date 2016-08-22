package com.epam.newsmanagement.app.model;

import java.sql.Timestamp;

/**
 * Comment model.
 */
public class Comment {
    private Long commentId;
    private News news;
    private String commentText;
    private Timestamp creationDate;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Comment comment = (Comment) o;

        if (commentId != null ? !commentId.equals(comment.commentId) : comment.commentId != null)
            return false;
        if (news != null ? !news.equals(comment.news) : comment.news != null)
            return false;
        if (commentText != null ? !commentText.equals(comment.commentText) : comment.commentText != null)
            return false;
        return creationDate != null ? creationDate.equals(comment.creationDate) : comment.creationDate == null;

    }

    @Override
    public int hashCode() {
        int result = commentId != null ? commentId.hashCode() : 0;
        result = 31 * result + (news != null ? news.hashCode() : 0);
        result = 31 * result + (commentText != null ? commentText.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", news=" + news +
                ", commentText='" + commentText + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
