<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <meta charset="utf-8">

        <title>News Management | News</title>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/jquery.multiselect.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/assets/style.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/assets/prettify.css" />" rel="stylesheet">
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" rel="stylesheet">

        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/jquery.multiselect.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/assets/prettify.js" />"></script>

        <sec:csrfMetaTags/>
    </head>
    <body>
        <div id="container">
            <tiles:insertAttribute name="header" />
            <div id="crutch">
                <tiles:insertAttribute name="navbar" />
                <div id="content" class="scrollable">
                    <div id="filter-row">
                        <select id="authors" name="authors">
                            <option value="" disabled selected>Please select the author</option>
                            <c:forEach var="author" items="${notExpiredAuthors}">
                                <option value="${author.authorName}">${author.authorName}</option>
                            </c:forEach>
                        </select>

                        <select id="tags" multiple="multiple" name="tags">
                            <option value="" disabled>Please select the tags</option>
                            <c:forEach var="tag" items="${tags}">
                                <option value="${tag.tagName}">${tag.tagName}</option>
                            </c:forEach>
                        </select>

                        <button id="filter-button">Filter</button>
                        <button id="reset-button">Reset</button>
                    </div>
                    <div id="news-list">
                        <c:forEach var="news" items="${newsList}">
                            <div class="short-news">
                                <div class="short-news-title-row">
                                    <div class="short-news-title">
                                        <a href="/news-admin/${news.newsId}">
                                            ${news.title}
                                        </a>
                                    </div>
                                    <div class="short-news-authors">
                                        ( by
                                        <c:forEach var="author" items="${authorsByNewsId[news.newsId]}" varStatus="loopStatus">
                                            <c:choose>
                                                <c:when test="${loopStatus.first}">
                                                    <c:out value="${author.authorName}" />
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value=", ${author.authorName}" />
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        )
                                    </div>
                                    <div class="short-news-last-edit">
                                        <u><fmt:formatDate
                                                value="${not empty news.modificationDate ? news.modificationDate : news.creationDate}"
                                                dateStyle="long" />
                                        </u>
                                    </div>
                                </div>
                                <div class="short-news-short-text-row">
                                    ${news.shortText}
                                </div>
                                <div class="short-news-footer-row">
                                    <div class="short-news-tags">
                                        <c:forEach var="tag" items="${tagsByNewsId[news.newsId]}" varStatus="loopStatus">
                                            <c:choose>
                                                <c:when test="${loopStatus.first}">
                                                    <c:out value="${tag.tagName}" />
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value=", ${tag.tagName}" />
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                    <div class="short-news-others">
                                        <span style="color: #ff0000">Comments(${commentsCountByNewsId[news.newsId]})</span> <a href="#">Edit</a> <input type="checkbox" />
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <button id="delete-button">Delete</button>
                    </div>
                    <div id="pagination-row">
                        <ul class="pagination">
                            <li><a id="first-page" class="disabled-page-arrow" href="#">«</a></li>
                            <li><a id="previous-page" class="disabled-page-arrow" href="#">❮</a></li>
                            <c:forEach begin="1" end="${pagesCount > 5 ? 5 : pagesCount}" var="i">
                                <c:choose>
                                    <c:when test="${i == 1}">
                                        <li><a class="active" href="#">${i}</a></li>
                                    </c:when>
                                    <c:otherwise>
                                        <li><a href="#">${i}</a></li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <li><a id="next-page" class="${pagesCount == 1 ? 'disabled-page-arrow' : ''}" href="#">❯</a></li>
                            <li><a id="last-page" class="${pagesCount < 6 ? 'disabled-page-arrow' : ''}" href="#">»</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
        <script type="text/javascript" src="<c:url value="/resources/js/newsmanagement.js" />"></script>
    </body>
</html>
