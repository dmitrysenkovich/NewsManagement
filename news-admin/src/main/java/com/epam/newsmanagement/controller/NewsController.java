package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.AuthorService;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.service.TagService;
import com.epam.newsmanagement.app.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.util.List;

/**
 * News controller. Dispatches adding
 * and editing news.
 */
@Controller
public class NewsController {
    private static final Logger logger = Logger.getLogger(NewsController.class);

    @Autowired
    private AuthorService authorService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @RequestMapping(value = "/edit-news/{newsId}", method = RequestMethod.GET)
    public ModelAndView editNews(@PathVariable Long newsId) throws ServiceException {
        logger.info("Editing news GET request");

        ModelAndView modelAndView = new ModelAndView("news-edit");

        List<Author> notExpiredAuthors = authorService.getNotExpired();
        modelAndView.addObject("notExpiredAuthors", notExpiredAuthors);

        List<Tag> tags = tagService.getAll();
        modelAndView.addObject("tags", tags);

        News news = newsService.find(newsId);
        modelAndView.addObject("news", news);

        List<Author> newsAuthors = authorService.getAllByNews(news);
        modelAndView.addObject("newsAuthors", newsAuthors);

        List<Tag> newsTags = tagService.getAllByNews(news);
        modelAndView.addObject("newsTags", newsTags);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        String userName = userService.userNameByLogin(login);
        modelAndView.addObject("userName", userName);

        return modelAndView;
    }


    @RequestMapping(value = "/add-news", method = RequestMethod.GET)
    public ModelAndView addNews() throws ServiceException {
        logger.info("Adding news GET request");

        ModelAndView modelAndView = new ModelAndView("news-edit");

        List<Author> notExpiredAuthors = authorService.getNotExpired();
        modelAndView.addObject("notExpiredAuthors", notExpiredAuthors);

        List<Tag> tags = tagService.getAll();
        modelAndView.addObject("tags", tags);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        String userName = userService.userNameByLogin(login);
        modelAndView.addObject("userName", userName);

        return modelAndView;
    }


    @RequestMapping(value = "/news/save", method = RequestMethod.POST)
    @ResponseBody
    public Long save(@RequestBody String requestBody) throws IOException, ServiceException {
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        News news = objectMapper.convertValue(jsonNode.get("news"), News.class);
        List<Author> authors = objectMapper.convertValue(jsonNode.get("authors"), new TypeReference<List<Author>>(){});
        List<Tag> tags = objectMapper.convertValue(jsonNode.get("tags"), new TypeReference<List<Tag>>(){});

        if (news.getNewsId() != null) {
            logger.info("Save edited news POST request");

            newsService.update(news);
            newsService.updateNewsAuthorsAndTags(news, authors, tags);
        }
        else {
            logger.info("Save new news POST request");

            news = newsService.add(news, authors, tags);
        }

        return news.getNewsId();
    }
}
