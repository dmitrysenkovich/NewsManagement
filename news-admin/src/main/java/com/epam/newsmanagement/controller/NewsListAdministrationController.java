package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.dao.AuthorRepository;
import com.epam.newsmanagement.app.dao.CommentRepository;
import com.epam.newsmanagement.app.dao.NewsRepository;
import com.epam.newsmanagement.app.dao.TagRepository;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.AuthorService;
import com.epam.newsmanagement.app.service.CommentService;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.service.TagService;
import com.epam.newsmanagement.app.service.UserService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.model.NewsInfo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * News list controller. Responsible
 * for rendering news list page,
 * editing, viewing news and etc..
 */
@Controller
public class NewsListAdministrationController {
    private static final Logger logger = Logger.getLogger(NewsListAdministrationController.class);

    @Autowired
    private AuthorService authorService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/news-list-administration", method = RequestMethod.GET)
    public ModelAndView newsListAdministration() throws ServiceException {
        logger.info("News list administration GET request");

        ModelAndView modelAndView = new ModelAndView("news-list-administration");

        List<Author> notExpiredAuthors = authorService.getNotExpired();
        modelAndView.addObject("notExpiredAuthors", notExpiredAuthors);

        List<Tag> tags = tagService.getAll();
        modelAndView.addObject("tags", tags);

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPageIndex(1L);
        List<News> newsList = newsService.search(searchCriteria);
        modelAndView.addObject("newsList", newsList);

        NewsInfo newsInfo = fillNewsInfo(newsList, searchCriteria);
        modelAndView.addObject("authorsByNewsId", newsInfo.getAuthorsByNewsId());
        modelAndView.addObject("tagsByNewsId", newsInfo.getTagsByNewsId());
        modelAndView.addObject("commentsCountByNewsId", newsInfo.getCommentsCountByNewsId());
        modelAndView.addObject("pagesCount", newsInfo.getPagesCount());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        String userName = userService.userNameByLogin(login);
        modelAndView.addObject("userName", userName);

        return modelAndView;
    }


    @RequestMapping(value = "/news-list-administration/reset", method = RequestMethod.POST)
    @ResponseBody
    public NewsInfo reset() throws ServiceException {
        logger.info("Reset POST request");

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPageIndex(1L);
        List<News> newsList = newsService.search(searchCriteria);
        NewsInfo newsInfo = fillNewsInfo(newsList, searchCriteria);

        return newsInfo;
    }


    @RequestMapping(value = "/news-list-administration/filter", method = RequestMethod.POST)
    @ResponseBody
    public NewsInfo filter(@RequestBody SearchCriteria searchCriteria) throws ServiceException {
        logger.info("Filter POST request");

        List<News> newsList = newsService.search(searchCriteria);
        NewsInfo newsInfo = fillNewsInfo(newsList, searchCriteria);

        return newsInfo;
    }


    @RequestMapping(value = "/news-list-administration/page", method = RequestMethod.POST)
    @ResponseBody
    public NewsInfo page(@RequestBody SearchCriteria searchCriteria) throws ServiceException {
        logger.info("Page POST request");

        if (searchCriteria.getPageIndex() == null) {
            Long pagesCount = newsService.countPagesBySearchCriteria(searchCriteria);
            searchCriteria.setPageIndex(pagesCount);
        }

        List<News> newsList = newsService.search(searchCriteria);
        NewsInfo newsInfo = fillNewsInfo(newsList, searchCriteria);

        return newsInfo;
    }


    private NewsInfo fillNewsInfo(List<News> newsList, SearchCriteria searchCriteria) throws ServiceException {
        NewsInfo newsInfo = new NewsInfo();
        newsInfo.setNewsList(newsList);

        Map<Long, List<Author>> authorsByNewsId = new HashMap<>();
        for (News news : newsList) {
            List<Author> authorsByNews = authorService.getAllByNews(news);
            authorsByNewsId.put(news.getNewsId(), authorsByNews);
        }
        newsInfo.setAuthorsByNewsId(authorsByNewsId);

        Map<Long, List<Tag>> tagsByNewsId = new HashMap<>();
        for (News news : newsList) {
            List<Tag> tagsByNews = tagService.getAllByNews(news);
            tagsByNewsId.put(news.getNewsId(), tagsByNews);
        }
        newsInfo.setTagsByNewsId(tagsByNewsId);

        Map<Long, Long> commentsCountByNewsId = new HashMap<>();
        for (News news : newsList) {
            Long newsCommentsCount = commentService.countAllByNews(news);
            commentsCountByNewsId.put(news.getNewsId(), newsCommentsCount);
        }
        newsInfo.setCommentsCountByNewsId(commentsCountByNewsId);

        Long pagesCount = newsService.countPagesBySearchCriteria(searchCriteria);
        newsInfo.setPagesCount(pagesCount);

        return newsInfo;
    }
}
