package com.epam.app.service;

import com.epam.app.dao.TagRepository;
import com.epam.app.model.Tag;
import com.epam.app.service.impl.TagServiceImpl;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tag service test.
 */
public class TagServiceTest {
    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private Logger logger;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(TagServiceImpl.class, "logger", logger);
    }

    @Test
    public void added() {
        Tag tag = new Tag();
        tag.setTagId(1L);
        when(tagRepository.add(tag)).thenReturn(tag);
        tag = tagService.add(tag);

        assertEquals((Long) 1L, tag.getTagId());
        verify(logger).info(eq("Successfully added new tag"));
    }

    @Test
    public void notAdded() {
        Tag tag = new Tag();
        when(tagRepository.add(tag)).thenReturn(tag);
        tag = tagService.add(tag);

        assertNull(tag.getTagId());
        verify(logger).error(eq("Failed to add new tag"));
    }

    @Test
    public void found() {
        Tag tag = new Tag();
        tag.setTagId(1L);
        when(tagRepository.find(1L)).thenReturn(tag);
        tag = tagService.find(1L);

        assertEquals((Long) 1L, tag.getTagId());
        verify(logger).info(eq("Successfully found tag"));
    }

    @Test
    public void notFound() {
        when(tagRepository.find(1L)).thenReturn(null);
        Tag tag = tagService.find(1L);

        assertNull(tag);
        verify(logger).error(eq("Failed to find tag"));
    }

    @Test
    public void updated() {
        Tag tag = new Tag();
        tag.setTagId(1L);
        when(tagRepository.update(tag)).thenReturn(true);
        boolean updated = tagService.update(tag);

        assertTrue(updated);
        verify(logger).info(eq("Successfully updated tag"));
    }

    @Test
    public void notUpdated() {
        when(tagRepository.update(null)).thenReturn(false);
        boolean updated = tagService.update(null);

        assertFalse(updated);
        verify(logger).error(eq("Failed to update tag"));
    }

    @Test
    public void deleted() {
        Tag tag = new Tag();
        tag.setTagId(1L);
        when(tagRepository.delete(tag)).thenReturn(true);
        boolean deleted = tagService.delete(tag);

        assertTrue(deleted);
        verify(logger).info(eq("Successfully deleted tag"));
    }

    @Test
    public void notDeleted() {
        when(tagRepository.delete(null)).thenReturn(false);
        boolean deleted = tagService.delete(null);

        assertFalse(deleted);
        verify(logger).error(eq("Failed to delete tag"));
    }

    @Test
    public void addedAll() {
        List<Tag> tags = new LinkedList<Tag>();
        Tag tag1 = new Tag();
        tag1.setTagId(1L);
        tags.add(tag1);
        Tag tag2 = new Tag();
        tag2.setTagId(1L);
        tags.add(tag2);
        when(tagRepository.addAll(tags)).thenReturn(tags);
        tagService.addAll(tags);

        verify(logger).info(eq("Successfully added tags"));
    }

    @Test
    public void notAddedAll() {
        List<Tag> tags = new LinkedList<Tag>();
        tags.add(new Tag());
        tags.add(new Tag());
        when(tagRepository.addAll(tags)).thenReturn(tags);
        tagService.addAll(tags);

        verify(logger).error(eq("Failed to add tags"));
    }
}
