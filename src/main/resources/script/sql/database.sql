CREATE TABLE News
(
    news_id NUMBER(20) PRIMARY KEY,
    title NVARCHAR2(30) NOT NULL,
    short_text NVARCHAR2(100) NOT NULL,
    full_text NVARCHAR2(100) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    modification_date DATE NOT NULL
);

CREATE TABLE Tag
(
    tag_id NUMBER(20) PRIMARY KEY,
    tag_name NVARCHAR2(30) NOT NULL
);

CREATE TABLE News_Tag
(
    news_id NUMBER(20) NOT NULL,
    tag_id NUMBER(20) NOT NULL,
    FOREIGN KEY(news_id) REFERENCES News(news_id) ON DELETE CASCADE,
    FOREIGN KEY(tag_id) REFERENCES Tag(tag_id) ON DELETE CASCADE
);

CREATE TABLE Author
(
    author_id NUMBER(20) PRIMARY KEY,
    author_name NVARCHAR2(30) NOT NULL,
    expired TIMESTAMP
);

CREATE TABLE News_Author
(
    news_id NUMBER(20) NOT NULL,
    author_id NUMBER(20) NOT NULL,
    FOREIGN KEY(news_id) REFERENCES News(news_id) ON DELETE CASCADE,
    FOREIGN KEY(author_id) REFERENCES Author(author_id) ON DELETE CASCADE
);

CREATE TABLE Comments
(
    comment_id NUMBER(20) PRIMARY KEY,
    news_id NUMBER(20) NOT NULL,
    comment_text NVARCHAR2(100) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    FOREIGN KEY(news_id) REFERENCES News(news_id) ON DELETE CASCADE
);

CREATE TABLE User
(
    user_id NUMBER(20) PRIMARY KEY,
    user_name NVARCHAR2(50) NOT NULL,
    login VARCHAR2(30) NOT NULL,
    password VARCHAR2(30) NOT NULL
);

CREATE TABLE Roles
(
    user_id NUMBER(20) NOT NULL,
    role_name VARCHAR2(50) NOT NULL,
    FOREIGN KEY(user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE SEQUENCE seq_news START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER News_Insert_Trigger
BEFORE INSERT ON News
FOR EACH ROW
BEGIN
    IF (:NEW.news_id IS NULL)
    THEN
     :NEW.news_id := seq_news.NEXTVAL;
    END IF;
END;
/

CREATE SEQUENCE seq_author START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER Author_Insert_Trigger
BEFORE INSERT ON Author
FOR EACH ROW
BEGIN
    IF (:NEW.author_id IS NULL)
    THEN
     :NEW.author_id := seq_author.NEXTVAL;
    END IF;
END;
/

CREATE SEQUENCE seq_tag START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER Tag_Insert_Trigger
BEFORE INSERT ON Tag
FOR EACH ROW
BEGIN
    IF (:NEW.tag_id IS NULL)
    THEN
     :NEW.tag_id := seq_tag.NEXTVAL;
    END IF;
END;
/

CREATE SEQUENCE seq_comments START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER Comments_Insert_Trigger
BEFORE INSERT ON Comments
FOR EACH ROW
BEGIN
    IF (:NEW.comment_id IS NULL)
    THEN
     :NEW.comment_id := seq_comments.NEXTVAL;
    END IF;
END;
/

CREATE SEQUENCE seq_user START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER User_Insert_Trigger
BEFORE INSERT ON User
FOR EACH ROW
BEGIN
    IF (:NEW.user_id IS NULL)
    THEN
     :NEW.user_id := seq_user.NEXTVAL;
    END IF;
END;
/
