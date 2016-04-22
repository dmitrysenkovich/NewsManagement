CREATE TABLE NEWS
(
    NEWS_ID NUMBER(20) PRIMARY KEY,
    TITLE NVARCHAR2(100) NOT NULL,
    SHORT_TEXT NVARCHAR2(500) NOT NULL,
    FULL_TEXT NCLOB NOT NULL,
    CREATION_DATE TIMESTAMP NOT NULL,
    MODIFICATION_DATE DATE NOT NULL
);

CREATE TABLE TAGS
(
    TAG_ID NUMBER(20) PRIMARY KEY,
    TAG_NAME NVARCHAR2(50) NOT NULL
);

CREATE TABLE NEWS_TAG
(
    NEWS_ID NUMBER(20) NOT NULL,
    TAG_ID NUMBER(20) NOT NULL,
    FOREIGN KEY(NEWS_ID) REFERENCES NEWS(NEWS_ID) ON DELETE CASCADE,
    FOREIGN KEY(TAG_ID) REFERENCES TAGS(TAG_ID) ON DELETE CASCADE
);

CREATE TABLE AUTHORS
(
    AUTHOR_ID NUMBER(20) PRIMARY KEY,
    AUTHOR_NAME NVARCHAR2(30) NOT NULL,
    EXPIRED TIMESTAMP
);

CREATE TABLE NEWS_AUTHOR
(
    NEWS_ID NUMBER(20) NOT NULL,
    AUTHOR_ID NUMBER(20) NOT NULL,
    FOREIGN KEY(NEWS_ID) REFERENCES NEWS(NEWS_ID) ON DELETE CASCADE,
    FOREIGN KEY(AUTHOR_ID) REFERENCES AUTHORS(AUTHOR_ID) ON DELETE CASCADE
);

CREATE TABLE COMMENTS
(
    COMMENT_ID NUMBER(20) PRIMARY KEY,
    NEWS_ID NUMBER(20) NOT NULL,
    COMMENT_TEXT NCLOB NOT NULL,
    CREATION_DATE TIMESTAMP NOT NULL,
    FOREIGN KEY(NEWS_ID) REFERENCES NEWS(NEWS_ID) ON DELETE CASCADE
);

CREATE TABLE ROLES
(
    ROLE_ID NUMBER(20) PRIMARY KEY,
    ROLE_NAME VARCHAR2(50) NOT NULL
);

CREATE TABLE USERS
(
    USER_ID NUMBER(20) PRIMARY KEY,
    ROLE_ID NUMBER(20) NOT NULL,
    USER_NAME NVARCHAR2(50) NOT NULL,
    LOGIN VARCHAR2(30) NOT NULL,
    PASSWORD VARCHAR2(30) NOT NULL,
    FOREIGN KEY(ROLE_ID) REFERENCES ROLES(ROLE_ID) ON DELETE CASCADE
);

CREATE SEQUENCE NEWS_IDS_SEQUENCE START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER NEWS_INSERT_TRIGGER
BEFORE INSERT ON NEWS
FOR EACH ROW
BEGIN
    :NEW.NEWS_ID := NEWS_IDS_SEQUENCE.NEXTVAL;
END;
/

CREATE SEQUENCE AUTHORS_IDS_SEQUENCE START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER AUTHORS_INSERT_TRIGGER
BEFORE INSERT ON AUTHORS
FOR EACH ROW
BEGIN
    :NEW.AUTHOR_ID := AUTHORS_IDS_SEQUENCE.NEXTVAL;
END;
/

CREATE SEQUENCE TAGS_IDS_SEQUENCE START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER TAGS_INSERT_TRIGGER
BEFORE INSERT ON TAGS
FOR EACH ROW
BEGIN
    :NEW.TAG_ID := TAGS_IDS_SEQUENCE.NEXTVAL;
END;
/

CREATE SEQUENCE COMMENTS_IDS_SEQUENCE START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER COMMENTS_INSERT_TRIGGER
BEFORE INSERT ON COMMENTS
FOR EACH ROW
BEGIN
    :NEW.COMMENT_ID := COMMENTS_IDS_SEQUENCE.NEXTVAL;
END;
/

CREATE SEQUENCE USERS_IDS_SEQUENCE START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER USERS_INSERT_TRIGGER
BEFORE INSERT ON USERS
FOR EACH ROW
BEGIN
    :NEW.USER_ID := USERS_IDS_SEQUENCE.NEXTVAL;
END;
/

CREATE SEQUENCE ROLES_IDS_SEQUENCE START WITH 1 INCREMENT BY 1 NOMAXVALUE;

CREATE TRIGGER ROLES_INSERT_TRIGGER
BEFORE INSERT ON ROLES
FOR EACH ROW
BEGIN
    :NEW.ROLE_ID := ROLES_IDS_SEQUENCE.NEXTVAL;
END;
/
