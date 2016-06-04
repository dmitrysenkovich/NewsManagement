package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.service.AuthorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Authors controller. Responsible
 * for managing authors.
 */
@Controller
public class AuthorsController {
    private static final Logger logger = Logger.getLogger(AuthorsController.class);

    @Autowired
    private AuthorService authorService;


    /**
     * Dispatches authors page request.
     * @return authors model and view.
     * @throws ServiceException
     */
    @RequestMapping(value = "/authors", method = RequestMethod.GET)
    public ModelAndView tags() throws ServiceException {
        logger.info("Authors GET request");

        ModelAndView modelAndView = new ModelAndView("authors");

        List<Author> authors = authorService.getAll();
        modelAndView.addObject("authors", authors);

        return modelAndView;
    }


    /**
     * Adds new author to database.
     * @param author author to be added.
     * @return new author id if there's
     * no author with such name.
     * @throws ServiceException
     */
    @RequestMapping(value = "/authors/add", method = RequestMethod.POST)
    @ResponseBody
    public Long add(@RequestBody Author author) throws ServiceException {
        logger.info("Add author POST request");

        boolean exists = authorService.exists(author);
        if (!exists) {
            author = authorService.add(author);
            return author.getAuthorId();
        }
        return null;
    }


    /**
     * Updates author name.
     * @param author author which
     * name will be updated.
     * @return true if there's no
     * author with new author's name.
     * @throws ServiceException
     */
    @RequestMapping(value = "/authors/update", method = RequestMethod.POST)
    @ResponseBody
    public boolean update(@RequestBody Author author) throws ServiceException {
        logger.info("Update author POST request");

        boolean exists = authorService.exists(author);
        if (!exists)
            authorService.update(author);
        return exists;
    }


    /**
     * Makes author expired.
     * @param author author that
     * will become expired.
     * @throws ServiceException
     */
    @RequestMapping(value = "/authors/expire", method = RequestMethod.POST)
    @ResponseBody
    public void expire(@RequestBody Author author) throws ServiceException {
        logger.info("Delete author POST request");

        authorService.makeAuthorExpired(author);
    }
}