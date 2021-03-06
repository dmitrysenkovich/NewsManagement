<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="true" contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <meta charset="utf-8">

        <c:choose>
            <c:when test="${empty sessionScope or sessionScope.language == 'en' or empty sessionScope.language}">
                <fmt:setLocale value="en_US" scope="session" />
            </c:when>
            <c:otherwise>
                <fmt:setLocale value="ru_RU" scope="session" />
            </c:otherwise>
        </c:choose>
        <c:set var="localeCode" value="${pageContext.response.locale}" />
        <script type="text/javascript">var localeCode = '${localeCode}';</script>

        <title><spring:message code="news_list.header" /></title>

        <link rel="stylesheet" href="<c:url value="/resources/css/style.css" />" />
        <link rel="stylesheet" href="<c:url value="/resources/css/jquery.multiselect.css" />" />
        <link rel="stylesheet" href="<c:url value="/resources/assets/style.css" />" />
        <link rel="stylesheet" href="<c:url value="/resources/assets/prettify.css" />" />
        <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" />

        <script type="text/javascript" src="<c:url value="/resources/js/jquery-2.0.0.min.js" />"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/jquery.multiselect.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/assets/prettify.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/jquery.i18n.properties.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/localization.js" />"></script>
    </head>
    <body>
        <div id="container">
            <tiles:insertAttribute name="header" />
            <div id="crutch">
                <div id="user-content" class="scrollable">
                    <div id="filter-row">
                        <select id="authors" multiple="multiple" name="authors">
                            <option value="default" disabled><spring:message code="news_list.authors_prompt" /></option>
                            <c:forEach var="author" items="${notExpiredAuthors}">
                                <option value="${author.authorId}">${author.authorName}</option>
                            </c:forEach>
                        </select>

                        <select id="tags" multiple="multiple" name="tags">
                            <option value="default" disabled><spring:message code="news_list.tags_prompt" /></option>
                            <c:forEach var="tag" items="${tags}">
                                <option value="${tag.tagId}">${tag.tagName}</option>
                            </c:forEach>
                        </select>

                        <button id="filter-button"><spring:message code="news_list.filter" /></button>
                        <button id="reset-button"><spring:message code="news_list.reset" /></button>
                    </div>
                    <div id="news-list">
                        <c:if test="${empty newsList}">
                            <div id='no-news-found-message'><spring:message code="news_list.no_news" /></div>
                        </c:if>
                        <c:forEach var="news" items="${newsList}">
                            <div class="short-news">
                                <div class="short-news-title-row">
                                    <div class="short-news-title">
                                        <a href="/news-client/news/${news.newsId}">
                                            ${news.title}
                                        </a>
                                    </div>
                                    <div class="short-news-authors">
                                        <c:set var="authorsNames" value="" />
                                        <c:forEach var="author" items="${authorsByNewsId[news.newsId]}" varStatus="loopStatus">
                                            <c:choose>
                                                <c:when test="${loopStatus.first}">
                                                    <c:set var="authorsNames" value="${authorsNames}${author.authorName}" />
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="authorsNames" value="${authorsNames}, ${author.authorName}" />
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        (<spring:message code="news_list.by" /> ${authorsNames})
                                    </div>
                                    <div class="short-news-last-edit">
                                        <u><fmt:formatDate
                                                value="${news.modificationDate}"
                                                dateStyle="long" />
                                        </u>
                                    </div>
                                </div>
                                <div class="short-news-short-text-row">
                                        ${news.shortText.replace('\\n', '<br>')}
                                </div>
                                <div class="short-news-footer-row">
                                    <div class="short-news-tags">
                                        <c:if test="${not empty tagsByNewsId[news.newsId]}">
                                            <c:set var="tagsNames" value="" />
                                            <c:forEach var="tag" items="${tagsByNewsId[news.newsId]}" varStatus="loopStatus">
                                                <c:choose>
                                                    <c:when test="${loopStatus.first}">
                                                        <c:set var="tagsNames" value="${tagsNames}${tag.tagName}" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="tagsNames" value="${tagsNames}, ${tag.tagName}" />
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                            ${tagsNames}
                                        </c:if>
                                    </div>
                                    <div class="short-news-others">
                                        <span style="color: #ff0000"><spring:message code="news_list.comments" />(${commentsCountByNewsId[news.newsId]})</span> <a href="/news-client/news/${news.newsId}"><spring:message code="news_list.view" /></a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div id="user-pagination-row">
                        <ul class="pagination">
                            <li><a id="first-page" class="disabled-page-arrow" href="javascript:void(0)">«</a></li>
                            <li><a id="previous-page" class="disabled-page-arrow" href="javascript:void(0)">❮</a></li>
                            <c:forEach begin="1" end="${pagesCount > 5 ? 5 : pagesCount}" var="i">
                                <c:choose>
                                    <c:when test="${i == 1}">
                                        <li><a class="page-link active" href="javascript:void(0)">${i}</a></li>
                                    </c:when>
                                    <c:otherwise>
                                        <li><a href="javascript:void(0)" class="page-link">${i}</a></li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <li><a id="next-page" class="${pagesCount <= 1 ? 'disabled-page-arrow' : ''}" href="javascript:void(0)">❯</a></li>
                            <li><a id="last-page" class="${pagesCount <= 1 ? 'disabled-page-arrow' : ''}" href="javascript:void(0)">»</a></li>
                        </ul>
                    </div>
                    <div id="news-list-bottom-space"></div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
        <script type="text/javascript" src="<c:url value="/resources/js/news-list.js" />"></script>
    </body>
</html>