news_id IN (SELECT news_id
                  FROM News_Tag
                  WHERE tag_id IN {0}
                  GROUP BY news_id
                  HAVING COUNT(*) = {1})
