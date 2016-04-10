package com.epam.app.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Search utils test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/utils-test-context.xml"})
public class SearchUtilsTest {
    @Autowired
    SearchUtils searchUtils;

    @Test
    public void searchCriteriaIsNull() {
        SearchCriteria searchCriteria = null;
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);

        assertNull(searchQuery);
    }

    @Test
    public void searchCriteriaAuthorIsNull() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setAuthorId(null);
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);

        assertNull(searchQuery);
    }

    @Test
    public void searchCriteriaTagsAreNull() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setTagIds(null);
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);

        assertNull(searchQuery);
    }

    @Test
    public void searchCriteriaAuthorIsNullAndNoTags() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setAuthorId(null);
        searchCriteria.setTagIds(new LinkedList<Long>());
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);

        assertNull(searchQuery);
    }

    @Test
    public void searchCriteriaIsValid() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setAuthorId(1L);
        List<Long> tags = new LinkedList<Long>();
        tags.add(1L);
        tags.add(2L);
        searchCriteria.setTagIds(tags);
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);

        assertNotNull(searchQuery);
        assertTrue(searchQuery.contains("author_id = 1"));
        assertTrue(searchQuery.contains("tag_id IN (1, 2)"));
        assertTrue(searchQuery.contains("COUNT(*) = 2"));
    }
}
