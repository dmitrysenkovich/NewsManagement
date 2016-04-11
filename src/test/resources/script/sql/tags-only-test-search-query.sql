SELECT *
FROM ((SELECT news_id, title, short_text,
            full_text, creation_date, modification_date, comments_count
       FROM News JOIN (SELECT news_id, COUNT(*) AS comments_count
                       FROM Comments
                       GROUP BY news_id) AS News_Stat USING(news_id))
UNION
(SELECT news_id, title, short_text,
            full_text, creation_date, modification_date, comments_count
FROM News
WHERE news_id NOT IN(SELECT news_id
                     FROM Comments))) All_News_Stat
WHERE news_id IN (SELECT news_id
                  FROM News_Tag
                  WHERE tag_id IN (1, 2)
                  GROUP BY news_id
                  HAVING COUNT(*) = 2)
ORDER BY comments_count DESC
