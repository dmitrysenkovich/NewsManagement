package com.epam.app;

import com.epam.app.dao.*;
import com.epam.app.dao.UserRepository;
import com.epam.app.model.*;
import com.epam.app.service.NewsTagService;
import com.epam.app.service.UserService;
import com.epam.app.service.NewsService;
import com.epam.app.service.UserService;
import com.epam.app.utils.SearchCriteria;
import com.epam.app.utils.SearchUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 4/6/16.
 */
public class Ololo {
    public void ololo() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-configuration.xml");
        NewsTagService newsTagService = (NewsTagService) context.getBean("newsTagService");
        NewsRepository newsRepository = (NewsRepository) context.getBean("newsRepository");
        TagRepository tagRepository = (TagRepository) context.getBean("tagRepository");
        News news = newsRepository.find(3L);
        Tag tag = tagRepository.find(2L);
        newsTagService.delete(news, tag);
    }

    public static void main(String... args) {
        Ololo ololo = new Ololo();
        ololo.ololo();
    }
}
