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
import com.epam.newsmanagement.utils.InfoUtils;
import com.epam.newsmanagement.utils.NewsListInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

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
    private NewsService newsService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    @Autowired
    private InfoUtils infoUtils;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * Dispatches requests to news list.
     * @return news list model and view.
     * @throws ServiceException
     */
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

        NewsListInfo newsListInfo = infoUtils.getNewsListInfo(newsList, searchCriteria);
        modelAndView.addObject("authorsByNewsId", newsListInfo.getAuthorsByNewsId());
        modelAndView.addObject("tagsByNewsId", newsListInfo.getTagsByNewsId());
        modelAndView.addObject("commentsCountByNewsId", newsListInfo.getCommentsCountByNewsId());
        modelAndView.addObject("pagesCount", newsListInfo.getPagesCount());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        String userName = userService.userNameByLogin(login);
        modelAndView.addObject("userName", userName);

        return modelAndView;
    }


    /**
     * Resets news list to the
     * initial state.
     * @param request request.
     * @return list of some
     * first news in the database.
     * @throws ServiceException
     */
    @RequestMapping(value = "/news-list-administration/reset", method = RequestMethod.GET)
    @ResponseBody
    public NewsListInfo reset(HttpServletRequest request) throws ServiceException {
        logger.info("Reset GET request");

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPageIndex(1L);
        HttpSession session = request.getSession(false);
        session.setAttribute("searchCriteria", searchCriteria);

        List<News> newsList = newsService.search(searchCriteria);
        NewsListInfo newsListInfo = infoUtils.getNewsListInfo(newsList, searchCriteria);

        return newsListInfo;
    }


    /**
     * Filters news according to
     * provided search criteria,
     * @param searchCriteriaInString
     * JSON string representation of search criteria.
     * @param request request.
     * @return some first news info
     * satisfying passed search criteria.
     * @throws ServiceException
     * @throws IOException
     */
    @RequestMapping(value = "/news-list-administration/filter", method = RequestMethod.GET)
    @ResponseBody
    public NewsListInfo filter(@RequestParam("searchCriteria") String searchCriteriaInString,
                               HttpServletRequest request) throws ServiceException, IOException {
        logger.info("Filter GET request");

        SearchCriteria searchCriteria = objectMapper.readValue(searchCriteriaInString, SearchCriteria.class);
        HttpSession session = request.getSession(false);
        session.setAttribute("searchCriteria", searchCriteria);

        List<News> newsList = newsService.search(searchCriteria);
        NewsListInfo newsListInfo = infoUtils.getNewsListInfo(newsList, searchCriteria);

        return newsListInfo;
    }


    /**
     * Returns certain page of news
     * satisfying the search criteria.
     * @param searchCriteriaInString
     * JSON string representation of search criteria.
     * @param request request.
     * @return needed news page with
     * news satisfying the search criteria.
     * @throws ServiceException
     * @throws IOException
     */
    @RequestMapping(value = "/news-list-administration/page", method = RequestMethod.GET)
    @ResponseBody
    public NewsListInfo page(@RequestParam("searchCriteria") String searchCriteriaInString,
                             HttpServletRequest request) throws ServiceException, IOException {
        logger.info("Page GET request");

        SearchCriteria searchCriteria = objectMapper.readValue(searchCriteriaInString, SearchCriteria.class);
        if (searchCriteria.getPageIndex() == null) {
            Long pagesCount = newsService.countPagesBySearchCriteria(searchCriteria);
            searchCriteria.setPageIndex(pagesCount);
        }
        HttpSession session = request.getSession(false);
        session.setAttribute("searchCriteria", searchCriteria);

        List<News> newsList = newsService.search(searchCriteria);
        NewsListInfo newsListInfo = infoUtils.getNewsListInfo(newsList, searchCriteria);

        return newsListInfo;
    }


    /**
     * Deletes list of news.
     * @param requestBody contains
     * JSON representation of news ids
     * which are to be deleted and search
     * criteria that is needed for retrieving
     * next news according to current filter.
     * This news will be rendered instead of
     * deleted ones.
     * @param request request.
     * @return Next news list according to
     * the search criteria.
     * @throws IOException
     * @throws ServiceException
     */
    @RequestMapping(value = "/news-list-administration/delete", method = RequestMethod.POST)
    @ResponseBody
    public NewsListInfo deleteNews(@RequestBody String requestBody,
                                   HttpServletRequest request) throws IOException, ServiceException {
        logger.info("Delete news POST request");

        JsonNode jsonNode = objectMapper.readTree(requestBody);
        List<Long> newsIds = objectMapper.convertValue(jsonNode.get("newsIds"), new TypeReference<List<Long>>(){});
        SearchCriteria searchCriteria = objectMapper.convertValue(jsonNode.get("searchCriteria"), SearchCriteria.class);

        newsService.deleteAll(newsIds);

        Long pagesCount = newsService.countPagesBySearchCriteria(searchCriteria);
        if (pagesCount == 0) {
            return null;
        }
        else if (searchCriteria.getPageIndex() - 1 == pagesCount) {
            searchCriteria.setPageIndex(searchCriteria.getPageIndex() - 1);
        }

        HttpSession session = request.getSession(false);
        session.setAttribute("searchCriteria", searchCriteria);

        List<News> newsList = newsService.search(searchCriteria);
        NewsListInfo newsListInfo = infoUtils.getNewsListInfo(newsList, searchCriteria);

        return newsListInfo;
    }
}
