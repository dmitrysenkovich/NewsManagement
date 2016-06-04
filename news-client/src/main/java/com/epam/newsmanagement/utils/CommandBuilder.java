package com.epam.newsmanagement.utils;

import com.epam.newsmanagement.command.Command;
import com.epam.newsmanagement.command.error.ErrorCommand;
import com.epam.newsmanagement.command.news.CommentCommand;
import com.epam.newsmanagement.command.news.NewsCommand;
import com.epam.newsmanagement.command.news.NextNewsCommand;
import com.epam.newsmanagement.command.news.PreviousNewsCommand;
import com.epam.newsmanagement.command.newslist.FilterCommand;
import com.epam.newsmanagement.command.newslist.NewsListCommand;
import com.epam.newsmanagement.command.newslist.PageCommand;
import com.epam.newsmanagement.command.newslist.ResetCommand;
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
        if ((uri.equals("/news-management/news-list") || uri.equals("/news-management/")) && method.equals("GET"))
            return newsListCommand;
        else if (uri.equals("/news-management/news-list/reset") && method.equals("GET"))
            return resetCommand;
        else if (uri.equals("/news-management/news-list/filter") && method.equals("GET"))
            return filterCommand;
        else if (uri.equals("/news-management/news-list/page") && method.equals("GET"))
            return pageCommand;
        else if (uri.startsWith("/news-management/news/") && method.equals("GET")) {
            int idIndex = uri.lastIndexOf("/");
            String newsIdInString = uri.substring(idIndex + 1);
            Long newsId = null;
            try {
                newsId = Long.parseLong(newsIdInString);
            } catch (NumberFormatException e) {}
            if (newsId != null)
                return newsCommand;
        }
        else if (uri.equals("/news-management/news/previous") && method.equals("GET"))
            return previousNewsCommand;
        else if (uri.equals("/news-management/news/next") && method.equals("GET"))
            return nextNewsCommand;
        else if (uri.equals("/news-management/news/comment") && method.equals("POST"))
            return commentCommand;
        return errorCommand;
    }
}
