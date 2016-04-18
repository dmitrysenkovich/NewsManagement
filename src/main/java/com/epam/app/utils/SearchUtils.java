package com.epam.app.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.List;

/**
 * Search utils.
 */
public class SearchUtils {
    private static final Logger logger = Logger.getLogger(SearchUtils.class.getName());

    private static final String MAIN_PART_FILE_NAME = "search-main.sql";
    private static final String AUTHOR_PART_FILE_NAME = "search-author-part.sql";
    private static final String TAGS_PART_FILE_NAME = "search-tags-part.sql";

    private String MAIN_PART;
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
        MAIN_PART = scriptFileUtils.getScriptPart(searchScriptDirectoryPath, MAIN_PART_FILE_NAME, logger,
                "Error reading main part of search script");
        AUTHOR_PART = scriptFileUtils.getScriptPart(searchScriptDirectoryPath, AUTHOR_PART_FILE_NAME, logger,
                "Error reading author part of search script");
        TAGS_PART = scriptFileUtils.getScriptPart(searchScriptDirectoryPath, TAGS_PART_FILE_NAME, logger,
                "Error reading tags part of search script");
        if (!MAIN_PART.equals("") && !AUTHOR_PART.equals("") && !TAGS_PART.equals(""))
            logger.info("Successfully read all search script parts");
    }


    /**
     * Generates sql query for news search.
     * @param searchCriteria shows what author
     * and what tags must sought-for news contain.
     * @return sql query.
     */
    public String getSearchQuery(SearchCriteria searchCriteria) {
        if (searchCriteria == null)
            return null;

        if (searchCriteria.getAuthorId() == null &&
                (searchCriteria.getTagIds() == null || searchCriteria.getTagIds().isEmpty()))
            return null;

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

        String searchQuery = MessageFormat.format(MAIN_PART, authorMatchingChecking, and, tagsMatchingChecking);
        return searchQuery;
    }
}
