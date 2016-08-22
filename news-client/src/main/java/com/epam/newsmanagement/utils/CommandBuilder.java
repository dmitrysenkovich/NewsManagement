package com.epam.newsmanagement.utils;

import com.epam.newsmanagement.command.Command;
import com.epam.newsmanagement.command.error.ErrorCommand;
import com.epam.newsmanagement.command.comment.CommentCommand;
import com.epam.newsmanagement.command.page.NewsCommand;
import com.epam.newsmanagement.command.news.NextNewsCommand;
import com.epam.newsmanagement.command.news.PreviousNewsCommand;
import com.epam.newsmanagement.command.news.FilterCommand;
import com.epam.newsmanagement.command.page.NewsListCommand;
import com.epam.newsmanagement.command.news.PageCommand;
import com.epam.newsmanagement.command.news.ResetCommand;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Command builder. Creates
 * command depending on
 * request uri and http method.
 */
public class CommandBuilder {
    @Autowired
    private NewsListCommand newsListCommand;
    @Autowired
    private ResetCommand resetCommand;
    @Autowired
    private FilterCommand filterCommand;
    @Autowired
    private PageCommand pageCommand;
    @Autowired
    private NewsCommand newsCommand;
    @Autowired
    private NextNewsCommand nextNewsCommand;
    @Autowired
    private PreviousNewsCommand previousNewsCommand;
    @Autowired
    private CommentCommand commentCommand;
    @Autowired
    private ErrorCommand errorCommand;


    /**
     * Returns command according
     * to request uri and method.
     * @param request request.
     * @return appropriate command.
     */
    public Command getCommand(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if ("/news-client/".equals(uri) && "GET".equals(method))
            return newsListCommand;
        else if ("/news-client/news/reset".equals(uri) && "GET".equals(method))
            return resetCommand;
        else if ("/news-client/news/filter".equals(uri) && "GET".equals(method))
            return filterCommand;
        else if ("/news-client/news/page".equals(uri) && "GET".equals(method))
            return pageCommand;
        else if ("/news-client/news/previous".equals(uri) && "GET".equals(method))
            return previousNewsCommand;
        else if ("/news-client/news/next".equals(uri) && "GET".equals(method))
            return nextNewsCommand;
        else if ("/news-client/news/".startsWith(uri) && "GET".equals(method)) {
            int idIndex = uri.lastIndexOf('/');
            String newsIdInString = uri.substring(idIndex + 1);
            Long newsId = null;
            try {
                newsId = Long.parseLong(newsIdInString);
            } catch (NumberFormatException e) {}
            if (newsId != null)
                return newsCommand;
        }
        else if ("/news-client/comment/add".equals(uri) && "POST".equals(method))
            return commentCommand;
        else if ("/news-client/400".equals(uri) || "/news-client/403".equals(uri) ||
                "/news-client/404".equals(uri) || "/news-client/500".equals(uri))
            return errorCommand;
        return null;
    }
}
