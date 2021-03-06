package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.NewsService;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.utils.InfoUtils;
import com.epam.newsmanagement.utils.NewsEditInfo;
import com.epam.newsmanagement.utils.NewsInfo;
import com.epam.newsmanagement.utils.NewsListInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    private NewsService newsService;

    @Autowired
    private InfoUtils infoUtils;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * Dispatches certain news request.
     * @param newsId info about news
     * with this id will be retrieved.
     * @param searchCriteriaInString searchCriteria
     * encoded in string.
     * @return news model and view.
     * @throws ServiceException
     */
    @RequestMapping(value = "/news", method = RequestMethod.GET)
    @ResponseBody
    public NewsInfo news(@RequestParam Long newsId,
                         @RequestParam(name = "searchCriteria", required = false) String searchCriteriaInString)
            throws ServiceException, IOException {
        logger.info("News GET request");

        SearchCriteria searchCriteria = new SearchCriteria();
        if (searchCriteriaInString != null)
            searchCriteria = objectMapper.readValue(searchCriteriaInString, SearchCriteria.class);
        return infoUtils.getNewsInfo(newsId, searchCriteria);
    }

    /**
     * Returns info enough for editing news.
     * @param newsId id of the news that
     * will be edited.
     * @return news edit info.
     * @throws ServiceException
     */
    @RequestMapping(value = "/news/edit", method = RequestMethod.GET)
    @ResponseBody
    public NewsEditInfo newsEdit(@RequestParam Long newsId) throws ServiceException {
        logger.info("News edit GET request");

        return infoUtils.getNewsEditInfo(newsId);
    }


    /**
     * Resets news list to the
     * initial state.
     * @param request request.
     * @return list of some
     * first news in the database.
     * @throws ServiceException
     */
    @RequestMapping(value = "/news/reset", method = RequestMethod.GET)
    @ResponseBody
    public NewsListInfo reset(HttpServletRequest request) throws ServiceException {
        logger.info("Reset GET request");

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPageIndex(1L);
        HttpSession session = request.getSession(false);
        session.setAttribute("searchCriteria", searchCriteria);

        List<News> newsList = newsService.search(searchCriteria);

        return infoUtils.getNewsListInfo(newsList, searchCriteria);
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
    @RequestMapping(value = "/news/filter", method = RequestMethod.GET)
    @ResponseBody
    public NewsListInfo filter(@RequestParam("searchCriteria") String searchCriteriaInString,
                               HttpServletRequest request) throws ServiceException, IOException {
        logger.info("Filter GET request");

        SearchCriteria searchCriteria = objectMapper.readValue(searchCriteriaInString, SearchCriteria.class);
        HttpSession session = request.getSession(false);
        session.setAttribute("searchCriteria", searchCriteria);

        List<News> newsList = newsService.search(searchCriteria);
        return infoUtils.getNewsListInfo(newsList, searchCriteria);
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
    @RequestMapping(value = "/news/page", method = RequestMethod.GET)
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
        return infoUtils.getNewsListInfo(newsList, searchCriteria);
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
    @RequestMapping(value = "/news/delete", method = RequestMethod.POST)
    @ResponseBody
    public NewsListInfo delete(@RequestBody String requestBody,
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
        return infoUtils.getNewsListInfo(newsList, searchCriteria);
    }


    /**
     * Saves new news of updates existing.
     * @param requestBody new news
     * in JSON string.
     * @param response http response.
     * @return news id.
     * @throws IOException
     * @throws ServiceException
     */
    @RequestMapping(value = "/news/save", method = RequestMethod.PUT)
    @ResponseBody
    public News save(@RequestBody String requestBody, HttpServletResponse response) throws IOException, ServiceException {
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        News news = objectMapper.convertValue(jsonNode.get("news"), News.class);
        List<Author> authors = objectMapper.convertValue(jsonNode.get("authors"), new TypeReference<List<Author>>(){});
        List<Tag> tags = objectMapper.convertValue(jsonNode.get("tags"), new TypeReference<List<Tag>>(){});

        if (news.getNewsId() != null) {
            logger.info("Save edited news PUT request");

            try {
                newsService.update(news);
            } catch (ServiceException e) {
                if (e.getException() instanceof JpaOptimisticLockingFailureException)
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                return null;
            }
            newsService.updateNewsAuthorsAndTags(news, authors, tags);
        }
        else {
            logger.info("Save new news PUT request");

            news = newsService.add(news, authors, tags);
        }

        return news;
    }
}
