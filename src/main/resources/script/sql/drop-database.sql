DROP TRIGGER ROLES_INSERT_TRIGGER;
DROP TRIGGER USERS_INSERT_TRIGGER;
DROP TRIGGER COMMENTS_INSERT_TRIGGER;
DROP TRIGGER TAGS_INSERT_TRIGGER;
DROP TRIGGER AUTHORS_INSERT_TRIGGER;
DROP TRIGGER NEWS_INSERT_TRIGGER;

DROP SEQUENCE ROLES_IDS_SEQUENCE;
DROP SEQUENCE USERS_IDS_SEQUENCE;
DROP SEQUENCE COMMENTS_IDS_SEQUENCE;
DROP SEQUENCE TAGS_IDS_SEQUENCE;
DROP SEQUENCE AUTHORS_IDS_SEQUENCE;
DROP SEQUENCE NEWS_IDS_SEQUENCE;

DROP TABLE USERS;
DROP TABLE ROLES;
DROP TABLE COMMENTS;
DROP TABLE NEWS_AUTHOR;
DROP TABLE AUTHORS;
DROP TABLE NEWS_TAG;
DROP TABLE TAGS;
DROP TABLE NEWS;