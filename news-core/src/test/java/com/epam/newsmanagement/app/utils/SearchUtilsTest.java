package com.epam.newsmanagement.app.utils;

import com.epam.newsmanagement.app.utils.ScriptFileUtils;
import com.epam.newsmanagement.app.utils.SearchCriteria;
import com.epam.newsmanagement.app.utils.SearchUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Search utils test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/repository-test-context.xml"})
public class SearchUtilsTest {
    private static final String TEST_AUTHOR_AND_TAGS_SEARCH_SCRIPT_FILE_NAME = "author-and-tags-test-search-query.sql";
    private static final String TEST_AUTHOR_ONLY_SEARCH_SCRIPT_FILE_NAME = "author-only-test-search-query.sql";
    private static final String TEST_TAGS_ONLY_SEARCH_SCRIPT_FILE_NAME = "tags-only-test-search-query.sql";

    @Autowired
    private SearchUtils searchUtils;
    @Autowired
    private ScriptFileUtils scriptFileUtils;
    private String TEST_SEARCH_SCRIPT_DIRECTORY;

    @Before
    public void asd() {
        TEST_SEARCH_SCRIPT_DIRECTORY = scriptFileUtils.getTestSearchScriptDirectoryPath();
    }


    @Test
    public void searchCriteriaIsValidAuthorIsNotNullTagsAreNotNull() throws Exception {
        String testAuthorAndTagsSearchScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_AUTHOR_AND_TAGS_SEARCH_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setAuthorId(1L);
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        String generatedAuthorAndTagsSearchScript = searchUtils.getSearchQuery(searchCriteria);

        assertEquals(testAuthorAndTagsSearchScript, generatedAuthorAndTagsSearchScript);
    }

    @Test
    public void searchCriteriaIsValidAuthorIsNotNullTagsAreNull() throws Exception {
        String testAuthorOnlySearchScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_AUTHOR_ONLY_SEARCH_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setAuthorId(1L);
        String generatedAuthorOnlySearchScript = searchUtils.getSearchQuery(searchCriteria);

        assertEquals(testAuthorOnlySearchScript, generatedAuthorOnlySearchScript);
    }

    @Test
    public void searchCriteriaIsValidAuthorIsNullTagsAreNotNull() throws Exception {
        String testTagsOnlySearchScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_TAGS_ONLY_SEARCH_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        String generatedTagsOnlySearchScript = searchUtils.getSearchQuery(searchCriteria);

        assertEquals(testTagsOnlySearchScript, generatedTagsOnlySearchScript);
    }

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
        searchCriteria.setTagIds(new LinkedList<>());
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);

        assertNull(searchQuery);
    }
}
