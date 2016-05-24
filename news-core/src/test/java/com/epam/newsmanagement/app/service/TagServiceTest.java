package com.epam.newsmanagement.app.service;

import com.epam.newsmanagement.app.dao.TagRepository;
import com.epam.newsmanagement.app.exception.DaoException;
import com.epam.newsmanagement.app.exception.ServiceException;
import com.epam.newsmanagement.app.model.Tag;
import com.epam.newsmanagement.app.service.impl.TagServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
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
        when(tagRepository.add(tag)).thenReturn(1L);
        tag = tagService.add(tag);

        assertEquals((Long) 1L, tag.getTagId());
    }


    @Test(expected = ServiceException.class)
    public void notAdded() throws Exception {
        doThrow(new DaoException()).when(tagRepository).add(any(Tag.class));
        tagService.add(new Tag());
    }


    @Test
    public void found() throws Exception {
        Tag tag = new Tag();
        tag.setTagId(1L);
        when(tagRepository.find(1L)).thenReturn(tag);
        tag = tagService.find(1L);

        assertEquals((Long) 1L, tag.getTagId());
    }


    @Test(expected = ServiceException.class)
    public void notFound() throws Exception {
        doThrow(new DaoException()).when(tagRepository).find(any(Long.class));
        tagService.find(1L);
    }


    @Test
    public void updated() throws Exception {
        doNothing().when(tagRepository).update(any(Tag.class));
        tagService.update(new Tag());
    }


    @Test(expected = ServiceException.class)
    public void notUpdated() throws Exception {
        doThrow(new DaoException()).when(tagRepository).update(any(Tag.class));
        tagService.update(new Tag());
    }


    @Test
    public void deleted() throws Exception {
        doNothing().when(tagRepository).delete(any(Tag.class));
        tagService.delete(new Tag());
    }


    @Test(expected = ServiceException.class)
    public void notDeleted() throws Exception {
        doThrow(new DaoException()).when(tagRepository).delete(any(Tag.class));
        tagService.delete(new Tag());
    }
}
