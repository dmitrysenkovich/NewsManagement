package com.epam.app.utils;

import java.text.MessageFormat;
import java.util.List;

/**
 * Search utils.
 */
public class SearchUtils {
    private static final String mainPart = "SELECT * FROM ((SELECT news_id, title, short_text, " +
            "                                   full_text, creation_date, modification_date, comments_count " +
            "                               FROM News JOIN (SELECT news_id, COUNT(*) AS comments_count " +
            "                                               FROM Comments " +
            "                                               GROUP BY news_id) AS News_Stat USING(news_id))" +
            "                               UNION " +
            "                               (SELECT news_id, title, short_text, " +
            "                                    full_text, creation_date, modification_date, 0 AS comments_count " +
            "                                FROM News " +
            "                                WHERE news_id NOT IN(SELECT news_id " +
            "                                                    FROM Comments))) All_News_Stat " +
            "                               WHERE {0} {1} {2}" +
            "                               ORDER BY comments_count DESC;";
    private static final String authorPart = "EXISTS(SELECT * FROM News_Author NA " +
            "                                        WHERE NA.news_id = news_id AND author_id = {0})";
    private static final String tagsPart = "news_id IN (SELECT news_id " +
            "                                           FROM News_Tag" +
            "                                           WHERE tag_id IN {0}" +
            "                                           GROUP BY news_id" +
            "                                           HAVING COUNT(*) = {1})";


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
            authorMatchingChecking = MessageFormat.format(authorPart, searchCriteria.getAuthorId());

        String tagsMatchingChecking = "";
        if (searchCriteria.getTagIds() != null && !searchCriteria.getTagIds().isEmpty()) {
            List<Long> tagIds = searchCriteria.getTagIds();
            StringBuilder tagIdsInString = new StringBuilder();
            for (Long tagId : tagIds)
                tagIdsInString.append(tagId + ", ");
            tagIdsInString.insert(0, "(");
            tagIdsInString.deleteCharAt(tagIdsInString.length() - 1);
            tagIdsInString.setCharAt(tagIdsInString.length() - 1, ')');
            tagsMatchingChecking = MessageFormat.format(tagsPart, tagIdsInString, tagIds.size());
        }

        String and = "";
        if (!authorMatchingChecking.equals("") && !tagsMatchingChecking.equals(""))
            and = " AND ";

        String searchQuery = MessageFormat.format(mainPart, authorMatchingChecking, and, tagsMatchingChecking);
        return searchQuery;
    }
}
