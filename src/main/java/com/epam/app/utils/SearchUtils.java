package com.epam.app.utils;

/**
 * Search utils.
 */
public class SearchUtils {
    public String getSearchQuery(SearchCriteria searchCriteria) {
        if (searchCriteria.getAuthorId() == null &&
                (searchCriteria.getTagIds() == null || searchCriteria.getTagIds().isEmpty()))
            return null;

        StringBuilder searchQuery = new StringBuilder();
        searchQuery.append("SELECT news_id, title, short_text, " +
                "full_text, creation_date, modification_date" +
                "FROM News JOIN (SELECT news_id, COUNT(*) AS comments_count" +
                "               FROM Comments" +
                "               GROUP BY news_id) AS News_Stat USING(news_id)" +
                "WHERE ");
        String authorMathingChecking = "";
        if (searchCriteria.getAuthorId() != null)
            authorExisting = "EXISTS(SELECT * FROM NewsAuthor NA WHERE NA.news_id = news_id AND author_id = )" +
                    searchCriteria.getAuthorId();
        String tagsMathingChecking = "";
        if (searchCriteria.getTagIds() != null && !searchCriteria.getTagIds().isEmpty()) {

        }

                "ORDER BY comments_count DESC;");
    }
}
