package com.epam.newsmanagement.controller;

import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Author;
import com.epam.newsmanagement.app.service.AuthorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * Adds new author to database.
     * @param author author to be added.
     * @return new author id if there's
     * no author with such name.
     * @throws ServiceException
     */
    @RequestMapping(value = "/authors/add", method = RequestMethod.PUT)
    @ResponseBody
    public Long add(@RequestBody Author author) throws ServiceException {
        logger.info("Add author PUT request");

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
    @RequestMapping(value = "/authors/update", method = RequestMethod.PUT)
    @ResponseBody
    public boolean update(@RequestBody Author author) throws ServiceException {
        logger.info("Update author PUT request");

        boolean exists = authorService.exists(author);
        if (!exists)
            authorService.update(author);
        return exists;
    }


    /**
     * Makes author expired.
     * @param authorId authorId
     * of author that will become expired.
     * @throws ServiceException
     */
    @RequestMapping(value = "/authors/expire/{authorId}", method = RequestMethod.POST)
    @ResponseBody
    public void expire(@PathVariable Long authorId) throws ServiceException {
        logger.info("Delete author POST request");

        Author author = new Author();
        author.setAuthorId(authorId);
        authorService.makeAuthorExpired(author);
    }
}
