package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.dao.TagRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.impl.TagServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Tag service test.
 */
public class TagServiceTest {
    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void added() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(1L);
        when(tagRepository.save(tag)).thenReturn(tag);
        tag = tagService.add(tag);

        assertEquals((Long) 1L, tag.getTagId());
    }


    @Test(expected = ServiceException.class)
    public void didNotAdd() throws Exception {
        doThrow(new DaoException()).when(tagRepository).save(any(Tag.class));
        tagService.add(new Tag());
    }


    @Test
    public void found() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(1L);
        when(tagRepository.findOne(1L)).thenReturn(tag);
        tag = tagService.find(1L);

        assertEquals((Long) 1L, tag.getTagId());
    }


    @Test(expected = ServiceException.class)
    public void didNotFind() throws Exception {
        doThrow(new DaoException()).when(tagRepository).findOne(any(Long.class));
        tagService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        doNothing().when(tagRepository).save(any(Tag.class));
        tagService.update(new Tag());
    }


    @Test(expected = ServiceException.class)
    public void didNotUpdate() throws Exception {
        doThrow(new DaoException()).when(tagRepository).save(any(Tag.class));
        tagService.update(new Tag());
    }


    @Test
    public void deleted() throws Exception {
        doNothing().when(tagRepository).delete(any(Tag.class));
        tagService.delete(new Tag());
    }


    @Test(expected = ServiceException.class)
    public void didNotDelete() throws Exception {
        doThrow(new DaoException()).when(tagRepository).delete(any(Tag.class));
        tagService.delete(new Tag());
    }


    @Test
    public void gotAllTagsByNews() throws Exception {
        when(tagRepository.getAllByNews(any(News.class))).thenReturn(new ArrayList<>());
        List<Tag> allTagsByNews = tagService.getAllByNews(new News());

        assertNotNull(allTagsByNews);
    }


    @Test(expected = ServiceException.class)
    public void didNotGetAllTagsByNews() throws Exception {
        doThrow(new DaoException()).when(tagRepository).getAllByNews(any(News.class));
        tagService.getAllByNews(new News());
    }


    @Test
    public void gotAllTags() throws Exception {
        when(tagRepository.findAll()).thenReturn(new ArrayList<>());
        List<Tag> allTags = tagService.getAll();

        assertNotNull(allTags);
    }


    @Test(expected = ServiceException.class)
    public void didNotGetAllTags() throws Exception {
        doThrow(new DaoException()).when(tagRepository).findAll();
        tagService.getAll();
    }


    @Test
    public void checkedTagExistence() throws Exception {
        when(tagRepository.exists(any(String.class))).thenReturn(true);
        boolean exists = tagService.exists(new Tag());

        assertNotNull(exists);
    }


    @Test(expected = ServiceException.class)
    public void didNotCheckTagExistence() throws Exception {
        doThrow(new DaoException()).when(tagRepository).exists(any(String.class));
        tagService.exists(new Tag());
    }
}
