package com.epam.app.model;

import java.sql.Timestamp;

/**
 * Comment model.
 */
public class Comment {
    private Long commentId;
    private Long newsId;
    private String commentText;
    private Timestamp creationDate;

    public Comment() {}

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
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
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;

        Comment comment = (Comment) object;

        if (commentId != null ? !commentId.equals(comment.commentId) : comment.commentId != null)
            return false;
        if (newsId != null ? !newsId.equals(comment.newsId) : comment.newsId != null)
            return false;
        if (commentText != null ? !commentText.equals(comment.commentText) : comment.commentText != null)
            return false;
        return creationDate != null ? creationDate.equals(comment.creationDate) : comment.creationDate == null;

    }

    @Override
    public int hashCode() {
        int result = commentId != null ? commentId.hashCode() : 0;
        result = 31 * result + (newsId != null ? newsId.hashCode() : 0);
        result = 31 * result + (commentText != null ? commentText.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", newsId=" + newsId +
                ", commentText='" + commentText + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
