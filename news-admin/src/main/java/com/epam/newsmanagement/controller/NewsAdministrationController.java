package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Comment;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.service.CommentService;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.service.UserService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.utils.InfoUtils;
import com.epam.newsmanagement.utils.NewsInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * News administration controller.
 */
@Controller
public class NewsAdministrationController {
    private static final Logger logger = Logger.getLogger(NewsAdministrationController.class);

    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private UserService userService;

    @Autowired
    private InfoUtils infoUtils;


    /**
     * Dispatches certain news request.
     * @param newsId info about news
     * with this id will be retrieved.
     * @param request request.
     * @return news model and view.
     * @throws ServiceException
     */
    @RequestMapping(value = "/view-news/{newsId}", method = RequestMethod.GET)
    public ModelAndView news(@PathVariable Long newsId, HttpServletRequest request) throws ServiceException {
        logger.info("News GET request");

        ModelAndView modelAndView = new ModelAndView("news-administration");

        News news = newsService.find(newsId);

        HttpSession session = request.getSession(false);
        SearchCriteria searchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
        if (searchCriteria == null)
            searchCriteria = new SearchCriteria();
        session.setAttribute("searchCriteria", searchCriteria);

        Long newsRowNumber = newsService.rowNumberBySearchCriteria(searchCriteria, news);
        session.setAttribute("newsRowNumber", newsRowNumber);

        NewsInfo newsInfo = infoUtils.getNewsInfo(news, searchCriteria, newsRowNumber);
        modelAndView.addObject("news", news);
        modelAndView.addObject("authors", newsInfo.getAuthors());
        modelAndView.addObject("comments", newsInfo.getComments());

        if (newsInfo.getFirst())
            modelAndView.addObject("first", true);
        if (newsInfo.getLast())
            modelAndView.addObject("last", true);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        String userName = userService.userNameByLogin(login);
        modelAndView.addObject("userName", userName);

        return modelAndView;
    }


    /**
     * Dispatches request to the previous
     * news according current search criteria.
     * @param request request.
     * @return previous news information.
     * @throws ServiceException
     */
    @RequestMapping(value = "/view-news/previous", method = RequestMethod.GET)
    @ResponseBody
    public NewsInfo previousNews(HttpServletRequest request) throws ServiceException {
        logger.info("Previous news GET request");

        HttpSession session = request.getSession(false);
        SearchCriteria searchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
        Long newsRowNumber = (Long) session.getAttribute("newsRowNumber");
        newsRowNumber--;
        session.setAttribute("newsRowNumber", newsRowNumber);
        searchCriteria.setPageIndex(newsRowNumber);
        searchCriteria.setPageSize(1L);
        News news = newsService.search(searchCriteria).get(0);

        NewsInfo newsInfo = infoUtils.getNewsInfo(news, searchCriteria, newsRowNumber);

        return newsInfo;
    }


    /**
     * Dispatches request to the next
     * news according current search criteria.
     * @param request request.
     * @return next news information.
     * @throws ServiceException
     */
    @RequestMapping(value = "/view-news/next", method = RequestMethod.GET)
    @ResponseBody
    public NewsInfo nextNews(HttpServletRequest request) throws ServiceException {
        logger.info("Next news GET request");

        HttpSession session = request.getSession(false);
        SearchCriteria searchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
        Long newsRowNumber = (Long) session.getAttribute("newsRowNumber");
        newsRowNumber++;
        session.setAttribute("newsRowNumber", newsRowNumber);
        searchCriteria.setPageIndex(newsRowNumber);
        searchCriteria.setPageSize(1L);
        News news = newsService.search(searchCriteria).get(0);

        NewsInfo newsInfo = infoUtils.getNewsInfo(news, searchCriteria, newsRowNumber);

        return newsInfo;
    }


    /**
     * Deletes news comment.
     * @param commentId specifies comment
     * that is to be deleted.
     * @throws ServiceException
     */
    @RequestMapping(value = "/view-news/delete", method = RequestMethod.POST)
    @ResponseBody
    public void delete(@RequestBody Long commentId) throws ServiceException {
        logger.info("Delete POST request");

        Comment comment = new Comment();
        comment.setCommentId(commentId);
        commentService.delete(comment);
    }


    /**
     * Adds new comment to the news.
     * @param commentText new comment
     * text
     * @param request request.
     * @return new comment.
     * @throws ServiceException
     */
    @RequestMapping(value = "/view-news/comment", method = RequestMethod.POST)
    @ResponseBody
    public Comment comment(@RequestBody String commentText, HttpServletRequest request) throws ServiceException {
        logger.info("Comment POST request");

        HttpSession session = request.getSession(false);
        SearchCriteria searchCriteria = (SearchCriteria) session.getAttribute("searchCriteria");
        Long newsRowNumber = (Long) session.getAttribute("newsRowNumber");
        searchCriteria.setPageIndex(newsRowNumber);
        searchCriteria.setPageSize(1L);
        News news = newsService.search(searchCriteria).get(0);

        Comment comment = new Comment();
        comment.setCommentText(commentText);

        comment = commentService.add(news, comment);

        return comment;
    }
}
