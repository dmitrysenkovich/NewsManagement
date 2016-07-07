package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.dao.TagRepository;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.News;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.impl.TagServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.RecoverableDataAccessException;

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
        doThrow(new RecoverableDataAccessException("")).when(tagRepository).save(any(Tag.class));
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
        doThrow(new RecoverableDataAccessException("")).when(tagRepository).findOne(any(Long.class));
        tagService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        when(tagRepository.save(any(Tag.class))).thenReturn(new Tag());
        tagService.update(new Tag());
    }


    @Test(expected = ServiceException.class)
    public void didNotUpdate() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(tagRepository).save(any(Tag.class));
        tagService.update(new Tag());
    }


    @Test
    public void deleted() throws Exception {
        doNothing().when(tagRepository).delete(any(Tag.class));
        tagService.delete(new Tag());
    }


    @Test(expected = ServiceException.class)
    public void didNotDelete() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(tagRepository).delete(any(Tag.class));
        tagService.delete(new Tag());
    }


    @Test
    public void foundAllTagsByNews() throws Exception {
        when(tagRepository.findAllByNews(any(News.class))).thenReturn(new ArrayList<>());
        List<Tag> allTagsByNews = tagService.findAllByNews(new News());

        assertNotNull(allTagsByNews);
    }


    @Test(expected = ServiceException.class)
    public void didNotFindAllTagsByNews() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(tagRepository).findAllByNews(any(News.class));
        tagService.findAllByNews(new News());
    }


    @Test
    public void foundAllTags() throws Exception {
        when(tagRepository.findAll()).thenReturn(new ArrayList<>());
        List<Tag> allTags = tagService.findAll();

        assertNotNull(allTags);
    }


    @Test(expected = ServiceException.class)
    public void didNotFindAllTags() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(tagRepository).findAll();
        tagService.findAll();
    }


    @Test
    public void checkedTagExistence() throws Exception {
        when(tagRepository.exists(any(String.class))).thenReturn(true);
        boolean exists = tagService.exists(new Tag());

        assertNotNull(exists);
    }


    @Test(expected = ServiceException.class)
    public void didNotCheckTagExistence() throws Exception {
        doThrow(new RecoverableDataAccessException("")).when(tagRepository).exists(any(String.class));
        tagService.exists(new Tag());
    }
}
