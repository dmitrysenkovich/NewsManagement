EXISTS(SELECT * FROM News_Author NA
             WHERE NA.news_id = All_News_Stat.news_id AND author_id = {0})
