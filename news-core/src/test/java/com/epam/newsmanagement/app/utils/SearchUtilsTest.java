package com.epam.newsmanagement.app.utils;

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
    private static final String TEST_AUTHORS_AND_TAGS_SEARCH_SCRIPT_FILE_NAME = "authors-and-tags-test-search-query.sql";
    private static final String TEST_AUTHORS_ONLY_SEARCH_SCRIPT_FILE_NAME = "authors-only-test-search-query.sql";
    private static final String TEST_TAGS_ONLY_SEARCH_SCRIPT_FILE_NAME = "tags-only-test-search-query.sql";
    private static final String TEST_AUTHORS_AND_TAGS_PAGE_SCRIPT_FILE_NAME = "authors-and-tags-test-page-query.sql";
    private static final String TEST_AUTHORS_AND_TAGS_COUNT_SCRIPT_FILE_NAME = "authors-and-tags-test-count-query.sql";
    private static final String TEST_AUTHORS_AND_TAGS_ROW_NUMBER_SCRIPT_FILE_NAME = "authors-and-tags-test-row-number-query.sql";

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
                TEST_AUTHORS_AND_TAGS_SEARCH_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorIds = new LinkedList<>();
        authorIds.add(1L);
        searchCriteria.setAuthorIds(authorIds);
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        String generatedAuthorsAndTagsSearchScript = searchUtils.getSearchQuery(searchCriteria);

        assertEquals(testAuthorAndTagsSearchScript, generatedAuthorsAndTagsSearchScript);
    }
    

    @Test
    public void searchCriteriaIsValidAuthorsAreNotNullTagsAreNull() throws Exception {
        String testAuthorOnlySearchScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_AUTHORS_ONLY_SEARCH_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorIds = new LinkedList<>();
        authorIds.add(1L);
        searchCriteria.setAuthorIds(authorIds);
        String generatedAuthorsOnlySearchScript = searchUtils.getSearchQuery(searchCriteria);

        assertEquals(testAuthorOnlySearchScript, generatedAuthorsOnlySearchScript);
    }


    @Test
    public void searchCriteriaIsValidAuthorsAreNullTagsAreNotNull() throws Exception {
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
    public void pageSearchCriteriaPageIndexNotNullPageSizeNotNull() {
        String testAuthorsAndTagsPageScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_AUTHORS_AND_TAGS_PAGE_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorsIds = new LinkedList<>();
        authorsIds.add(1L);
        searchCriteria.setAuthorIds(authorsIds);
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        searchCriteria.setPageSize(5L);
        searchCriteria.setPageIndex(1L);
        String generatedAuthorsAndTagsPageScript = searchUtils.getSearchQuery(searchCriteria);

        assertEquals(testAuthorsAndTagsPageScript, generatedAuthorsAndTagsPageScript);
    }


    @Test
    public void pageSearchCriteriaPageIndexIsLessThaOnePageSizeIsNotNull() {
        String testAuthorsAndTagsPageScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_AUTHORS_AND_TAGS_PAGE_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorsIds = new LinkedList<>();
        authorsIds.add(1L);
        searchCriteria.setAuthorIds(authorsIds);
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        searchCriteria.setPageSize(5L);
        searchCriteria.setPageIndex(0L);
        String generatedAuthorsAndTagsPageScript = searchUtils.getSearchQuery(searchCriteria);

        assertEquals(testAuthorsAndTagsPageScript, generatedAuthorsAndTagsPageScript);
    }


    @Test
    public void pageSearchCriteriaPageIndexIsNotNullPageSizeIsNull() {
        String testAuthorsAndTagsPageScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_AUTHORS_AND_TAGS_PAGE_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorsIds = new LinkedList<>();
        authorsIds.add(1L);
        searchCriteria.setAuthorIds(authorsIds);
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        searchCriteria.setPageIndex(1L);
        String generatedAuthorsAndTagsPageScript = searchUtils.getSearchQuery(searchCriteria);

        assertEquals(testAuthorsAndTagsPageScript, generatedAuthorsAndTagsPageScript);
    }


    @Test
    public void pageSearchCriteriaPageIndexIsNotNullPageSizeIsLessThanOne() {
        String testAuthorsAndTagsPageScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_AUTHORS_AND_TAGS_PAGE_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorsIds = new LinkedList<>();
        authorsIds.add(1L);
        searchCriteria.setAuthorIds(authorsIds);
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        searchCriteria.setPageSize(0L);
        searchCriteria.setPageIndex(1L);
        String generatedAuthorsAndTagsPageScript = searchUtils.getSearchQuery(searchCriteria);

        assertEquals(testAuthorsAndTagsPageScript, generatedAuthorsAndTagsPageScript);
    }


    @Test
    public void countSearchCriteriaIsValidAuthorIsNotNullTagsAreNotNullPageIndexIsNotNull() throws Exception {
        String testAuthorAndTagsCountScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_AUTHORS_AND_TAGS_COUNT_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorIds = new LinkedList<>();
        authorIds.add(1L);
        searchCriteria.setAuthorIds(authorIds);
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        searchCriteria.setPageIndex(5L);
        String generatedAuthorAndTagsCountScript = searchUtils.getCountQuery(searchCriteria);

        assertEquals(testAuthorAndTagsCountScript, generatedAuthorAndTagsCountScript);
    }


    @Test
    public void countSearchCriteriaIsValidAuthorIsNotNullTagsAreNotNullPageIndexIsNull() throws Exception {
        String testAuthorAndTagsCountScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_AUTHORS_AND_TAGS_COUNT_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorIds = new LinkedList<>();
        authorIds.add(1L);
        searchCriteria.setAuthorIds(authorIds);
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        String generatedAuthorAndTagsCountScript = searchUtils.getCountQuery(searchCriteria);

        assertEquals(testAuthorAndTagsCountScript, generatedAuthorAndTagsCountScript);
    }


    @Test
    public void countSearchCriteriaIsValidAuthorIsNotNullTagsAreNotNullPageIndexIsLessThanOne() throws Exception {
        String testAuthorAndTagsCountScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_AUTHORS_AND_TAGS_COUNT_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorIds = new LinkedList<>();
        authorIds.add(1L);
        searchCriteria.setAuthorIds(authorIds);
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        searchCriteria.setPageIndex(0L);
        String generatedAuthorAndTagsCountScript = searchUtils.getCountQuery(searchCriteria);

        assertEquals(testAuthorAndTagsCountScript, generatedAuthorAndTagsCountScript);
    }


    @Test
    public void rowNumberSearchCriteriaIsValidAuthorIsNotNullTagsAreNotNullNewsIdIsNotNull() throws Exception {
        String testAuthorAndTagsRowNumberScript = scriptFileUtils.getScriptPart(TEST_SEARCH_SCRIPT_DIRECTORY,
                TEST_AUTHORS_AND_TAGS_ROW_NUMBER_SCRIPT_FILE_NAME, null, null);
        SearchCriteria searchCriteria = new SearchCriteria();
        List<Long> authorIds = new LinkedList<>();
        authorIds.add(1L);
        searchCriteria.setAuthorIds(authorIds);
        List<Long> tagIds = new LinkedList<>();
        tagIds.add(1L);
        tagIds.add(2L);
        searchCriteria.setTagIds(tagIds);
        String generatedAuthorAndTagsRowNumberScript = searchUtils.getRowNumberQuery(searchCriteria, 1L);

        assertEquals(testAuthorAndTagsRowNumberScript, generatedAuthorAndTagsRowNumberScript);
    }


    @Test
    public void searchCriteriaIsNull() {
        SearchCriteria searchCriteria = null;
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);

        assertNull(searchQuery);
    }


    @Test
    public void searchCriteriaAuthorsAreNull() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setAuthorIds(null);
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
    public void searchCriteriaAuthorsAreNullAndNoTags() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setAuthorIds(null);
        searchCriteria.setTagIds(new LinkedList<>());
        String searchQuery = searchUtils.getSearchQuery(searchCriteria);

        assertNull(searchQuery);
    }


    @Test
    public void rowNumberSearchCriteriaNewsIdIsNull() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        String searchQuery = searchUtils.getRowNumberQuery(searchCriteria, null);

        assertNull(searchQuery);
    }
}
