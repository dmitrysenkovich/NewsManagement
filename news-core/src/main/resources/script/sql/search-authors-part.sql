NEWS_ID IN (SELECT NEWS_ID
                  FROM NEWS_AUTHOR
                  WHERE AUTHOR_ID IN {0}
                  GROUP BY NEWS_ID
                  HAVING COUNT(DISTINCT AUTHOR_ID) = {1})
