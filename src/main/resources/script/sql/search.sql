SELECT * FROM ((SELECT news_id, title, short_text,
            full_text, creation_date, modification_date
            FROM News JOIN (SELECT news_id, COUNT(*) AS comments_count
                           FROM Comments
                           GROUP BY news_id) AS News_Stat USING(news_id)
            ORDER BY comments_count DESC)
UNION
(SELECT news_id, title, short_text,
            full_text, creation_date, modification_date
FROM News
WHERE news_id NOT IN(SELECT news_id
                    FROM Comments))) All_News_Stat
WHERE EXISTS(SELECT * FROM News_Author NA WHERE NA.news_id = news_id AND author_id = 2)
  AND news_id IN (SELECT news_id
                 FROM News_Tag
                 WHERE tag_id IN (2, 3)
                 GROUP BY news_id
                 HAVING COUNT(*) = 2);
