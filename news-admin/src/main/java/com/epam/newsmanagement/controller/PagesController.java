package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.AuthorService;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.service.TagService;
import com.epam.newsmanagement.app.service.UserService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.utils.AuthorizationUtils;
import com.epam.newsmanagement.utils.InfoUtils;
import com.epam.newsmanagement.utils.NewsInfo;
import com.epam.newsmanagement.utils.NewsListInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Dispatches requests for certain pages.
 */
@Controller
public class PagesController {
    private static final Logger logger = Logger.getLogger(PagesController.class);

    @Autowired
    private AuthorService authorService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationUtils authorizationUtils;
    @Autowired
    private InfoUtils infoUtils;


    /**
     * Dispatches requests to news list.
     * @return news list model and view.
     * @throws ServiceException
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView newsListAdministration() throws ServiceException {
        logger.info("News list GET request");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken)
            return new ModelAndView("redirect:/login");

        ModelAndView modelAndView = new ModelAndView("news-list");

        List<Author> notExpiredAuthors = authorService.findNotExpired();
        modelAndView.addObject("notExpiredAuthors", notExpiredAuthors);

        List<Tag> tags = tagService.findAll();
        modelAndView.addObject("tags", tags);

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPageIndex(1L);
        List<News> newsList = newsService.search(searchCriteria);
        modelAndView.addObject("newsList", newsList);

        NewsListInfo newsListInfo = infoUtils.getNewsListInfo(newsList, searchCriteria);
        modelAndView.addObject("authorsByNewsId", newsListInfo.getAuthorsByNewsId());
        modelAndView.addObject("tagsByNewsId", newsListInfo.getTagsByNewsId());
        modelAndView.addObject("commentsCountByNewsId", newsListInfo.getCommentsCountByNewsId());
        modelAndView.addObject("pagesCount", newsListInfo.getPagesCount());

        String login = authorizationUtils.getCurrentUserLogin();
        String userName = userService.userNameByLogin(login);
        modelAndView.addObject("userName", userName);

        return modelAndView;
    }


    /**
     * Dispatches certain news request.
     * @param newsId info about news
     * with this id will be retrieved.
     * @param request request.
     * @return news model and view.
     * @throws ServiceException
     */
    @RequestMapping(value = "/news/{newsId}", method = RequestMethod.GET)
    public ModelAndView news(@PathVariable Long newsId, HttpServletRequest request) throws ServiceException {
        logger.info("News GET request");

        ModelAndView modelAndView = new ModelAndView("news");

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

        String login = authorizationUtils.getCurrentUserLogin();
        String userName = userService.userNameByLogin(login);
        modelAndView.addObject("userName", userName);

        return modelAndView;
    }


    /**
     * Dispatches news edit request.
     * @param newsId edit page with
     * the news defined by this id
     * will be rendered.
     * @return news edit page model and view.
     * @throws ServiceException
     */
    @RequestMapping(value = "/edit/{newsId}", method = RequestMethod.GET)
    public ModelAndView editNews(@PathVariable Long newsId) throws ServiceException {
        logger.info("Editing news GET request");

        ModelAndView modelAndView = new ModelAndView("news-edit");

        List<Author> notExpiredAuthors = authorService.findNotExpired();
        modelAndView.addObject("notExpiredAuthors", notExpiredAuthors);

        List<Tag> tags = tagService.findAll();
        modelAndView.addObject("tags", tags);

        News news = newsService.find(newsId);
        modelAndView.addObject("news", news);

        List<Author> newsAuthors = authorService.findAllByNews(news);
        modelAndView.addObject("newsAuthors", newsAuthors);

        List<Tag> newsTags = tagService.findAllByNews(news);
        modelAndView.addObject("newsTags", newsTags);

        String login = authorizationUtils.getCurrentUserLogin();
        String userName = userService.userNameByLogin(login);
        modelAndView.addObject("userName", userName);

        return modelAndView;
    }


    /**
     * Dispatches request for
     * adding new news page.
     * @return adding news model and view.
     * @throws ServiceException
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView addNews() throws ServiceException {
        logger.info("Adding news GET request");

        ModelAndView modelAndView = new ModelAndView("news-edit");

        List<Author> notExpiredAuthors = authorService.findNotExpired();
        modelAndView.addObject("notExpiredAuthors", notExpiredAuthors);

        List<Tag> tags = tagService.findAll();
        modelAndView.addObject("tags", tags);

        String login = authorizationUtils.getCurrentUserLogin();
        String userName = userService.userNameByLogin(login);
        modelAndView.addObject("userName", userName);

        return modelAndView;
    }


    /**
     * Dispatches authors page request.
     * @return authors model and view.
     * @throws ServiceException
     */
    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public ModelAndView authors() throws ServiceException {
        logger.info("Authors GET request");

        ModelAndView modelAndView = new ModelAndView("authors");

        List<Author> authors = authorService.findAll();
        modelAndView.addObject("authors", authors);

        return modelAndView;
    }


    /**
     * Dispatches requests to tags page.
     * @return tags page model and view.
     * @throws ServiceException
     */
    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public ModelAndView tags() throws ServiceException {
        logger.info("Tags GET request");

        ModelAndView modelAndView = new ModelAndView("tags");

        List<Tag> tags = tagService.findAll();
        modelAndView.addObject("tags", tags);

        return modelAndView;
    }
}
