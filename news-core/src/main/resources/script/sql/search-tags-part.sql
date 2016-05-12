NEWS_ID IN (SELECT NEWS_ID
                  FROM NEWS_TAG
                  WHERE TAG_ID IN {0}
                  GROUP BY NEWS_ID
                  HAVING COUNT(*) = {1})
