import urllib.request
import json
import re
import random
import string

from bs4 import BeautifulSoup
import names
from random_words import RandomNicknames
import strgen
import MySQLdb


REUTERS_SITE = 'http://www.reuters.com'
FIRST_MAIN_PAGE_URL = REUTERS_SITE + '/news/archive/politicsNews?view=page&page=2&pageSize=10'
SECOND_MAIN_PAGE_URL = REUTERS_SITE + '/news/archive/politicsNews?view=page&page=3&pageSize=10'
THIRD_MAIN_PAGE_URL = REUTERS_SITE + '/news/archive/politicsNews?view=page&page=4&pageSize=10'
REDDIT_NEWS_PAGE_URL = 'https://www.reddit.com/r/politics/comments/4ed2h8/clinton_angrily_refuses_to_release_wall_street/'

USERNAMES_COUNT = 30
COMMENTS_COUNT = 600
TAGS_COUNT_PER_NEWS = 8
AUTHORS_COUNT_PER_NEWS = 3

ROLES = ['ADMIN', 'USER', 'MODERATOR']

DATA_SQL_FILE_PATH = '../sql/data.sql'

INSERT_NEWS_ROW = 'INSERT INTO NEWS VALUES({0}, \'{1}\', \'{2}\', \'{3}\', TO_TIMESTAMP(\'{4}\', \'YYYY-MM-DD HH24:MI:SS\'), TO_DATE(\'{5}\', \'YYYY-MM-DD\'));\n'
INSERT_TAG_ROW = 'INSERT INTO TAGS VALUES({0}, \'{1}\');\n'
INSERT_NEWS_TAG_ROW = 'INSERT INTO NEWS_TAG VALUES({0}, {1});\n'
INSERT_AUTHOR_ROW = 'INSERT INTO AUTHORS VALUES({0}, \'{1}\', TO_TIMESTAMP(\'{2}\', \'YYYY-MM-DD HH24:MI:SS\'));\n'
INSERT_NEWS_AUTHOR_ROW = 'INSERT INTO NEWS_AUTHOR VALUES({0}, {1});\n'
INSERT_COMMENT_ROW = 'INSERT INTO COMMENTS VALUES({0}, {1}, \'{2}\', TO_TIMESTAMP(\'{3}\', \'YYYY-MM-DD HH24:MI:SS\'));\n'
INSERT_USER_ROW = 'INSERT INTO USERS VALUES({0}, {1}, \'{2}\', \'{3}\', \'{4}\');\n'
INSERT_ROLE_ROW = 'INSERT INTO ROLES VALUES({0}, \'{1}\');\n'


def get_html(url):
    response = urllib.request.urlopen(url)
    html = response.read()
    return html


def parse_reuter_main_page_html(reuter_main_page_html):
    titles = []
    short_texts = []
    news_links = []
    soup = BeautifulSoup(reuter_main_page_html, 'html.parser')
    story_contents = soup.find_all('div', { 'class' : 'story-content' })
    for i in range(10):
        story_content = story_contents[i]
        titles.append(story_content.h3.a.getText())
        short_texts.append(story_content.p.getText())
        news_links.append(story_content.h3.a['href'])
    return titles, short_texts, news_links


def parse_news_page_html(news_page_html):
    authors = []
    creation_date = ''
    modification_date = ''
    full_text = ''
    tags = []
    soup = BeautifulSoup(news_page_html, 'html.parser')
    meta_information_json_in_string = soup.find('script', { 'type' : 'application/ld+json' }).getText()
    meta_information_json = json.loads(meta_information_json_in_string)
    if 'dateCreated' not in meta_information_json:
        meta_information_json = meta_information_json['article']
    creation_date = meta_information_json['dateCreated']
    creation_date = creation_date.replace('T', ' ')[:-5]
    tags = meta_information_json['keywords']
    modification_date = creation_date[:10]
    author_string = soup.find('meta', { 'property' : 'og:article:author' })['content']
    author_string = author_string.replace('By ', '').replace(' and', ',')
    authors = [author.strip() for author in author_string.split(',')]
    raw_paragraphs = soup.find('span', { 'id' : 'articleText' }).find_all('p')
    for raw_paragraph in raw_paragraphs:
        raw_paragraph_text = str(raw_paragraph)
        pattern = re.compile(r'<.*?>')
        raw_paragraph_text = pattern.sub('', raw_paragraph_text)
        pattern = re.compile(r'.*?\(Reporting.*')
        raw_paragraph_text = pattern.sub('', raw_paragraph_text)
        pattern = re.compile(r'.*?\(Click here.*\)')
        clean_text = pattern.sub('', raw_paragraph_text)
        if re.search('[a-zA-Z]', clean_text):
            full_text += clean_text + '\n'
    full_text = full_text.rstrip()
    return authors, creation_date, modification_date, full_text, tags


def get_comments_from_reddit_news(reddit_news_html):
    comments = []
    soup = BeautifulSoup(reddit_news_html, 'html.parser')
    mds = soup.find_all('div', { 'class' : 'md' })
    for i in range(COMMENTS_COUNT):
        md = random.choice(mds)
        comment = md.p.getText();
        comments.append(comment)
    return comments


def generate_usernames_logins_and_passwords():
    print('Generating usernames..')
    usernames = []
    for _ in range(USERNAMES_COUNT):
        usernames.append(names.get_first_name() + ' ' + names.get_full_name())
    print('Generated usernames')

    print('Generating logins..')
    rn = RandomNicknames()
    logins = []
    for _ in range(USERNAMES_COUNT):
        logins.append(rn.random_nick(gender='u'))
    print('Generated logins')

    print('Generating passwords..')
    passwords = []
    for _ in range(USERNAMES_COUNT):
        passwords.append(''.join(random.choice(string.ascii_uppercase + string.digits) for x in range(10)))
    print('Generated passwords')

    return usernames, logins, passwords


def generate_dataset(full_texts, titles, short_texts, creation_dates, \
        modification_dates, tags, authors, comments, usernames, logins, passwords):
    data_sql_file = open(DATA_SQL_FILE_PATH, 'w')

    print('Adding news..')
    news_count = len(full_texts)
    for i in range(news_count):
        data_sql_file.write(INSERT_NEWS_ROW.format(i+1, MySQLdb.escape_string(titles[i]).decode('UTF-8'), \
            MySQLdb.escape_string(short_texts[i]).decode('UTF-8'), \
            MySQLdb.escape_string(full_texts[i]).decode('UTF-8'), creation_dates[i], modification_dates[i]))
    print('Added news')

    data_sql_file.write('\n')

    print('Adding tags..')
    tags_count = len(tags)
    for i in range(tags_count):
        data_sql_file.write(INSERT_TAG_ROW.format(i+1, MySQLdb.escape_string(tags[i]).decode('UTF-8')))
    print('Added tags')

    data_sql_file.write('\n')

    print('Adding news to tag relations..')
    for i in range(news_count):
        tags_count_for_news = random.randint(1, TAGS_COUNT_PER_NEWS)
        for j in range(tags_count_for_news):
            tag_id = random.randint(1, tags_count)
            data_sql_file.write(INSERT_NEWS_TAG_ROW.format(i+1, tag_id))
    print('Added news to tag relations')

    data_sql_file.write('\n')

    print('Adding authors..')
    authors_count = len(authors)
    creation_dates_count = len(creation_dates)
    print(authors_count)
    for i in range(authors_count):
        data_sql_file.write(INSERT_AUTHOR_ROW.format(i+1, MySQLdb.escape_string(authors[i]).decode('UTF-8'), creation_dates[i%creation_dates_count]))
    print('Added authors')

    data_sql_file.write('\n')

    print('Adding news to author relations..')
    for i in range(news_count):
        authors_count_for_news = random.randint(1, AUTHORS_COUNT_PER_NEWS)
        for j in range(authors_count_for_news):
            author_id = random.randint(1, authors_count)
            data_sql_file.write(INSERT_NEWS_AUTHOR_ROW.format(i+1, author_id))
    print('Added news to author relations')

    data_sql_file.write('\n')

    print('Adding comments..')
    comments_count = len(comments)
    for i in range(comments_count):
        news_id = random.randint(1, news_count)
        data_sql_file.write(INSERT_COMMENT_ROW.format(i+1, news_id, MySQLdb.escape_string(comments[i]).decode('UTF-8'), creation_dates[news_id-1]))
    print('Added comments')

    data_sql_file.write('\n')

    print('Adding roles..')
    roles_count = len(ROLES)
    for i in range(roles_count):
        data_sql_file.write(INSERT_ROLE_ROW.format(i+1, ROLES[i]))
    print('Added roles')

    data_sql_file.write('\n')

    print('Adding users..')
    for i in range(USERNAMES_COUNT):
        role_id = random.randint(1, len(ROLES))
        data_sql_file.write(INSERT_USER_ROW.format(i+1, role_id, MySQLdb.escape_string(usernames[i]).decode('UTF-8'), \
            MySQLdb.escape_string(logins[i]).decode('UTF-8'), MySQLdb.escape_string(passwords[i]).decode('UTF-8')))
    print('Added users')

    data_sql_file.close()


def main():
    print("Getting reuters main pages html..")
    first_main_page_html = get_html(FIRST_MAIN_PAGE_URL)
    second_main_page_html = get_html(SECOND_MAIN_PAGE_URL)
    third_main_page_html = get_html(THIRD_MAIN_PAGE_URL)
    print("Got reuters main pages html")

    print("Parsing reuters main pages html in titles, short_texts and news_links..")
    titles, short_texts, news_links = parse_reuter_main_page_html(first_main_page_html)
    another_titles, another_short_texts, another_news_links = parse_reuter_main_page_html(second_main_page_html)
    titles.extend(another_titles)
    short_texts.extend(another_short_texts)
    news_links.extend(another_news_links)
    another_titles, another_short_texts, another_news_links = parse_reuter_main_page_html(third_main_page_html)
    titles.extend(another_titles)
    short_texts.extend(another_short_texts)
    news_links.extend(another_news_links)
    print("Parsed reuters main pages html in titles, short_texts and news_links")

    print("Parsing reuters news pages html in authors, creation_dates, modification_dates, full_texts and tags..")
    authors = []
    creation_dates = []
    modification_dates = []
    full_texts = []
    tags = []
    for news_link in news_links:
        news_url = REUTERS_SITE + news_link
        news_page_html = get_html(news_url)
        news_authors, creation_date, modification_date, full_text, tags = parse_news_page_html(news_page_html)
        authors.extend(news_authors)
        creation_dates.append(creation_date)
        modification_dates.append(modification_date)
        full_texts.append(full_text)
        tags.extend(tags)
    tags = list(set(tags))
    print("Parsed reuters news pages html in authors, creation_dates, modification_dates, full_texts and tags\n")



    print("Getting reddit news page html..")
    reddit_news_page_html = get_html(REDDIT_NEWS_PAGE_URL)
    print("Got reddit news page html")

    print("Parsing reddit news page in comments..")
    comments = get_comments_from_reddit_news(reddit_news_page_html)
    print("Parsed reddit news page in comments\n")



    print('Generating usernames, logins, passwords')
    usernames, logins, passwords = generate_usernames_logins_and_passwords()
    print('Generated usernames, logins, passwords\n')



    print('Generating data sql file..')
    generate_dataset(full_texts, titles, short_texts, creation_dates, \
        modification_dates, tags, authors, comments, usernames, logins, passwords)
    print('Generated data sql file')


if __name__ == '__main__':
    main()
