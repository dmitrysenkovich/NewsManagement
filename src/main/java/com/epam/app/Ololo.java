package com.epam.app;

import com.epam.app.dao.impl.NewsRepositoryImpl;
import com.epam.app.model.News;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.*;

/**
 * Created by dmitry on 4/6/16.
 */
public class Ololo {
    private BasicDataSource dataSource;

    public void ololo() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-configuration.xml");
        NewsRepositoryImpl newsRepository = (NewsRepositoryImpl) context.getBean("newsRepository");
        News news = new News();
        news.setTitle("asd");
        news.setShortText("asd");
        news.setFullText("asd");
        news.setCreationDate(new Timestamp(new java.util.Date().getTime()));
        news.setModificationDate(new Date(new java.util.Date().getTime()));
        newsRepository.add(news);
        System.out.println(news.getNewsId());
        System.out.println(newsRepository.find(news.getNewsId()));
        news.setTitle("asdasdasdas");
        newsRepository.update(news);
        System.out.println(newsRepository.find(news.getNewsId()));
        newsRepository.delete(news);
    }

    public static void main(String... args) {
        Ololo ololo = new Ololo();
        ololo.ololo();
    }
}
