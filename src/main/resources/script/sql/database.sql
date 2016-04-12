CREATE TABLE News
(
    news_id NUMBER(20) PRIMARY KEY,
    title NVARCHAR2(30) NOT NULL,
    short_text NVARCHAR2(100) NOT NULL,
    full_text NVARCHAR2(100) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    modification_date DATE NOT NULL
);

CREATE TABLE Tags
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

CREATE TABLE Authors
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

CREATE TABLE Users
(
    user_id NUMBER(20) PRIMARY KEY,
    role_id NUMBER(20) NOT NULL,
    user_name NVARCHAR2(50) NOT NULL,
    login VARCHAR2(30) NOT NULL,
    password VARCHAR2(30) NOT NULL,
    FOREIGN KEY(role_id) REFERENCES Roles(role_id) ON DELETE CASCADE
);

CREATE TABLE Roles
(
    role_id NUMBER(20) PRIMARY KEY,
    role_name VARCHAR2(50) NOT NULL
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

CREATE SEQUENCE seq_authors START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER Authors_Insert_Trigger
BEFORE INSERT ON Authors
FOR EACH ROW
BEGIN
    IF (:NEW.author_id IS NULL)
    THEN
     :NEW.author_id := seq_authors.NEXTVAL;
    END IF;
END;
/

CREATE SEQUENCE seq_tags START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER Tags_Insert_Trigger
BEFORE INSERT ON Tags
FOR EACH ROW
BEGIN
    IF (:NEW.tag_id IS NULL)
    THEN
     :NEW.tag_id := seq_tags.NEXTVAL;
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

CREATE SEQUENCE seq_users START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER Users_Insert_Trigger
BEFORE INSERT ON Users
FOR EACH ROW
BEGIN
    IF (:NEW.user_id IS NULL)
    THEN
     :NEW.user_id := seq_users.NEXTVAL;
    END IF;
END;
/

CREATE SEQUENCE seq_roles START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER Roles_Insert_Trigger
BEFORE INSERT ON Users
FOR EACH ROW
BEGIN
    IF (:NEW.role_id IS NULL)
    THEN
     :NEW.role_id := seq_roles.NEXTVAL;
    END IF;
END;
/
