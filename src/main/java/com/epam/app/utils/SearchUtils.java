package com.epam.app.utils;

import java.util.List;

/**
 * Search utils.
 */
public class SearchUtils {
    public String getSearchQuery(SearchCriteria searchCriteria) {
        if (searchCriteria.getAuthorId() == null &&
                (searchCriteria.getTagIds() == null || searchCriteria.getTagIds().isEmpty()))
            return null;

        StringBuilder searchQuery = new StringBuilder();
        searchQuery.append("SELECT * FROM ((SELECT news_id, title, short_text, " +
                "                               full_text, creation_date, modification_date, comments_count " +
                "                           FROM News JOIN (SELECT news_id, COUNT(*) AS comments_count " +
                "                                           FROM Comments " +
                "                                           GROUP BY news_id) AS News_Stat USING(news_id))" +
                "           UNION " +
                "           (SELECT news_id, title, short_text, " +
                "                   full_text, creation_date, modification_date, 0 AS comments_count " +
                "           FROM News " +
                "           WHERE news_id NOT IN(SELECT news_id " +
                "                                FROM Comments))) All_News_Stat " +
                "           WHERE ");
        String authorMatchingChecking = "";
        if (searchCriteria.getAuthorId() != null)
            authorMatchingChecking = "EXISTS(SELECT * FROM News_Author NA WHERE NA.news_id = news_id AND author_id = " +
                    searchCriteria.getAuthorId() + ")";
        String tagsMatchingChecking = "";
        if (searchCriteria.getTagIds() != null && !searchCriteria.getTagIds().isEmpty()) {
            List<Integer> tagIds = searchCriteria.getTagIds();
            StringBuilder tagIdsInString = new StringBuilder();
            for (Integer tagId : tagIds)
                tagIdsInString.append(tagId + ", ");
            tagIdsInString.insert(0, "(");
            tagIdsInString.deleteCharAt(tagIdsInString.length() - 1);
            tagIdsInString.setCharAt(tagIdsInString.length() - 1, ')');
            tagsMatchingChecking = "news_id IN (SELECT news_id " +
            "                                   FROM News_Tag" +
            "                                   WHERE tag_id IN  " + tagIdsInString +
            "                                   GROUP BY news_id" +
            "                                   HAVING COUNT(*) = " + tagIds.size() + ")"
            ;
        }
        String and = "";
        if (!authorMatchingChecking.equals("") && !tagsMatchingChecking.equals(""))
            and = " AND ";
        searchQuery.append(authorMatchingChecking);
        searchQuery.append(and);
        searchQuery.append(tagsMatchingChecking);
        searchQuery.append("ORDER BY comments_count DESC;");
        return searchQuery.toString();
    }
}
