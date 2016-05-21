package com.epam.newsmanagement.app.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.List;

/**
 * Search utils.
 */
public class SearchUtils {
    private static final Logger logger = Logger.getLogger(SearchUtils.class.getName());

    private static final String SEARCH_MAIN_PART_FILE_NAME = "search-main.sql";
    private static final String COUNT_MAIN_PART_FILE_NAME = "count-main.sql";
    private static final String PAGE_MAIN_PART_FILE_NAME = "page-search-main.sql";
    private static final String ROW_NUMBER_SEARCH_MAIN_PART_FILE_NAME = "row-number-search-main.sql";
    private static final String AUTHOR_PART_FILE_NAME = "search-author-part.sql";
    private static final String TAGS_PART_FILE_NAME = "search-tags-part.sql";

    private static final Long DEFAULT_PAGE_INDEX = 0L;
    private static final Long DEFAULT_PAGE_SIZE = 5L;

    private String SEARCH_MAIN_PART;
    private String COUNT_MAIN_PART;
    private String PAGE_MAIN_PART;
    private String ROW_NUMBER_MAIN_PART;
    private String AUTHOR_PART;
    private String TAGS_PART;

    @Autowired
    private ScriptFileUtils scriptFileUtils;


    /**
     * Initializes script parts.
     */
    @PostConstruct
    public void init() {
        String searchScriptDirectoryPath = scriptFileUtils.getSearchScriptDirectoryPath();
        SEARCH_MAIN_PART = scriptFileUtils.getScriptPart(searchScriptDirectoryPath, SEARCH_MAIN_PART_FILE_NAME, logger,
                "Error reading main part of search script");
        COUNT_MAIN_PART = scriptFileUtils.getScriptPart(searchScriptDirectoryPath, COUNT_MAIN_PART_FILE_NAME, logger,
                "Error reading main part of count script");
        PAGE_MAIN_PART = scriptFileUtils.getScriptPart(searchScriptDirectoryPath, PAGE_MAIN_PART_FILE_NAME, logger,
                "Error reading main part of page search script");
        ROW_NUMBER_MAIN_PART = scriptFileUtils.getScriptPart(searchScriptDirectoryPath, ROW_NUMBER_SEARCH_MAIN_PART_FILE_NAME, logger,
                "Error reading  main part of row number search script");
        AUTHOR_PART = scriptFileUtils.getScriptPart(searchScriptDirectoryPath, AUTHOR_PART_FILE_NAME, logger,
                "Error reading author part of search script");
        TAGS_PART = scriptFileUtils.getScriptPart(searchScriptDirectoryPath, TAGS_PART_FILE_NAME, logger,
                "Error reading tags part of search script");
        if (!SEARCH_MAIN_PART.equals("") && !COUNT_MAIN_PART.equals("") && !PAGE_MAIN_PART.equals("") &&
                !ROW_NUMBER_MAIN_PART.equals("") && !AUTHOR_PART.equals("") && !TAGS_PART.equals(""))
            logger.info("Successfully read all search script parts");
        else
            logger.error("Error while reading script parts");
    }


    /**
     * Checks whether search criteria is invalid.
     * @param searchCriteria search criteria to be checked.
     * @return test result.
     */
    private boolean simpleSearchCriteriaIsInvalid(SearchCriteria searchCriteria) {
        if (searchCriteria == null)
            return true;

        if (searchCriteria.getAuthorId() == null &&
                (searchCriteria.getTagIds() == null || searchCriteria.getTagIds().isEmpty()))
            return true;

        return false;
    }


    /**
     * Generates author and tags query parts.
     * @param searchCriteria specifies authors and tags.
     * @return
     */
    private String[] getQueryParts(SearchCriteria searchCriteria) {
        String authorMatchingChecking = "";
        if (searchCriteria.getAuthorId() != null)
            authorMatchingChecking = MessageFormat.format(AUTHOR_PART, searchCriteria.getAuthorId());

        String tagsMatchingChecking = "";
        if (searchCriteria.getTagIds() != null && !searchCriteria.getTagIds().isEmpty()) {
            List<Long> tagIds = searchCriteria.getTagIds();
            StringBuilder tagIdsInString = new StringBuilder();
            for (Long tagId : tagIds)
                tagIdsInString.append(tagId + ", ");
            tagIdsInString.insert(0, "(");
            tagIdsInString.deleteCharAt(tagIdsInString.length() - 1);
            tagIdsInString.setCharAt(tagIdsInString.length() - 1, ')');
            tagsMatchingChecking = MessageFormat.format(TAGS_PART, tagIdsInString, tagIds.size());
        }

        String and = "";
        if (!authorMatchingChecking.equals("") && !tagsMatchingChecking.equals(""))
            and = "\n    AND ";

        return new String[]{ authorMatchingChecking, and, tagsMatchingChecking };
    }


    /**
     * Generates sql query for news search.
     * @param searchCriteria shows what author
     * and what tags must sought-for news contain.
     * @return sql query.
     */
    private String simpleSearchQuery(SearchCriteria searchCriteria) {
        boolean simpleSearchCriteriaIsInvalid = simpleSearchCriteriaIsInvalid(searchCriteria);
        if (simpleSearchCriteriaIsInvalid)
            return null;

        String[] queryParts = getQueryParts(searchCriteria);

        String searchQuery = MessageFormat.format(SEARCH_MAIN_PART, queryParts[0], queryParts[1], queryParts[2]);
        return searchQuery;
    }


    /**
     * Generates sql query for search
     * for specific news page.
     * @param searchCriteria shows what author
     * and what tags must sought-for news contain.
     * Specifies news count and page index.
     * @return sql query.
     */
    private String pageSearchQuery(SearchCriteria searchCriteria) {
        Long pageIndex = searchCriteria.getPageIndex();
        Long pageSize = searchCriteria.getPageSize();
        if (pageIndex < 1)
            pageIndex = DEFAULT_PAGE_INDEX;
        if (pageSize == null || pageSize < 1)
            pageSize = DEFAULT_PAGE_SIZE;

        String[] queryParts = getQueryParts(searchCriteria);
        String where = "";
        if (!queryParts[0].isEmpty() || !queryParts[2].isEmpty())
            where = "WHERE ";

        String searchQuery = MessageFormat.format(PAGE_MAIN_PART, where,
                queryParts[0], queryParts[1], queryParts[2],
                ((pageIndex-1)*pageSize+1), (pageIndex)*pageSize);
            return searchQuery;
    }


    /**
     * Returns search query string satisfying
     * all search criteria clauses.
     * @param searchCriteria search criteria.
     * @return sql query.
     */
    public String getSearchQuery(SearchCriteria searchCriteria) {
        if (searchCriteria.getPageIndex() == null)
            return simpleSearchQuery(searchCriteria);
        else
            return pageSearchQuery(searchCriteria);
    }


    /**
     * Returns count query string satisfying
     * all search criteria clauses.
     * @param searchCriteria search criteria.
     * @return sql query.
     */
    public String getCountQuery(SearchCriteria searchCriteria) {
        String[] queryParts = getQueryParts(searchCriteria);

        String where = "";
        if (!queryParts[0].isEmpty() || !queryParts[2].isEmpty())
            where = "WHERE ";

        Long pageSize = searchCriteria.getPageSize();
        if (pageSize == null || pageSize < 1)
            pageSize = DEFAULT_PAGE_SIZE;

        String countQuery = MessageFormat.format(COUNT_MAIN_PART, pageSize,
                where, queryParts[0], queryParts[1], queryParts[2]);
        return countQuery;
    }


    /**
     * Returns row number query string satisfying
     * all search criteria clauses for certain news.
     * @param searchCriteria search criteria.
     * @param newsId news which row number are to be retrieved.
     * @return sql query.
     */
    public String getRowNumberQuery(SearchCriteria searchCriteria, Long newsId) {
        String[] queryParts = getQueryParts(searchCriteria);

        String where = "";
        if (!queryParts[0].isEmpty() || !queryParts[2].isEmpty())
            where = "WHERE ";

        String rowNumberQuery = MessageFormat.format(ROW_NUMBER_MAIN_PART,
                where, queryParts[0], queryParts[1], queryParts[2], newsId);
        return rowNumberQuery;
    }
}
