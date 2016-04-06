CREATE TABLE IF NOT EXISTS News
(
    news_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    title VARCHAR(30) NOT NULL,
    short_text VARCHAR(100) NOT NULL,
    full_text VARCHAR(100) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    modification_date DATE NOT NULL,
    PRIMARY KEY(news_id)
);

CREATE TABLE IF NOT EXISTS Tag
(
    tag_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    tag_name VARCHAR(30) NOT NULL,
    PRIMARY KEY(tag_id)
);

CREATE TABLE IF NOT EXISTS News_Tag
(
    news_id INT UNSIGNED NOT NULL,
    tag_id INT UNSIGNED NOT NULL,
    FOREIGN KEY(news_id) REFERENCES News(news_id) ON DELETE CASCADE,
    FOREIGN KEY(tag_id) REFERENCES Tag(tag_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Author
(
    author_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    author_name VARCHAR(30) NOT NULL,
    expired TIMESTAMP,
    PRIMARY KEY(author_id)
);

CREATE TABLE IF NOT EXISTS News_Author
(
    news_id INT UNSIGNED NOT NULL,
    author_id INT UNSIGNED NOT NULL,
    FOREIGN KEY(news_id) REFERENCES News(news_id) ON DELETE CASCADE,
    FOREIGN KEY(author_id) REFERENCES Author(author_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Comments
(
    comment_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    news_id INT UNSIGNED NOT NULL,
    comment_text VARCHAR(100) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    FOREIGN KEY(news_id) REFERENCES News(news_id) ON DELETE CASCADE,
    PRIMARY KEY(comment_id)
);

CREATE TABLE IF NOT EXISTS User
(
    user_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(50) NOT NULL,
    login VARCHAR(30) NOT NULL,
    password VARCHAR(30) NOT NULL,
    PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS Roles
(
    user_id INT UNSIGNED NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    FOREIGN KEY(user_id) REFERENCES User(user_id) ON DELETE CASCADE
);
