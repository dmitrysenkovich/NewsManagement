<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <meta charset="utf-8">

        <title><spring:message code="news_edit.header" /></title>

        <sec:csrfMetaTags/>
        <c:set var="localeCode" value="${pageContext.response.locale}" />
        <script>var localeCode = '${localeCode}';</script>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/jquery.multiselect.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/assets/style.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/assets/prettify.css" />" rel="stylesheet">
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" rel="stylesheet">

        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
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
                <tiles:insertAttribute name="navbar" />
                <div id="content" class="scrollable news-content">
                    <div id="${not empty news ? news.newsId : ''}" class="new-news">
                        <div id="new-news-title-row">
                            <div id="new-news-title-label">
                                <spring:message code="news_edit.title" />:
                            </div>
                            <div id="new-news-title">
                                <input id="new-news-textarea" type="text" value="${not empty news ? news.title : ''}">
                            </div>
                        </div>
                        <div id="date-row">
                            <div id="date-title">
                                <spring:message code="news_edit.date" />:
                            </div>
                            <div id="date">
                                <jsp:useBean id="now" class="java.util.Date" scope="request" />
                                <c:set var="newsDate" ><fmt:formatDate
                                        value="${now}"
                                        dateStyle="long" /></c:set>
                                <c:if test="${not empty news}">
                                    <c:set var="newsDate" ><fmt:formatDate
                                            value="${not empty news.modificationDate ? news.modificationDate : news.creationDate}"
                                            dateStyle="long" />
                                    </c:set>
                                </c:if>
                                <input id="date-textarea" type="text" value="${newsDate}" disabled>
                            </div>
                        </div>
                        <div id="short-text-row">
                            <div id="short-text-title">
                                <spring:message code="news_edit.brief" />:
                            </div>
                            <div id="short-text">
                                <textarea id="short-text-textarea" rows="4" >${not empty news ? news.shortText.replace('\\n', '&#13;&#10;') : ''}</textarea>
                            </div>
                        </div>
                        <div id="text-row">
                            <div id="text-title">
                                <spring:message code="news_edit.content" />:
                            </div>
                            <div id="text">
                                <textarea id="text-textarea" rows="6" >${not empty news ? news.fullText.replace('\\n', '&#13;&#10;') : ''}</textarea>
                            </div>
                        </div>
                    </div>
                    <div id="filter-row">
                        <select id="authors" multiple="multiple" name="authors">
                            <option value="default" disabled><spring:message code="news_edit.authors_prompt" /></option>
                            <c:choose>
                                <c:when test="${empty news}">
                                    <c:forEach var="author" items="${notExpiredAuthors}">
                                        <option value="${author.authorId}">${author.authorName}</option>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="author" items="${notExpiredAuthors}">
                                        <option value="${author.authorId}" ${newsAuthors.contains(author) ? 'selected' : ''}>${author.authorName}</option>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </select>

                        <select id="tags" multiple="multiple" name="tags">
                            <option value="default" disabled><spring:message code="news_edit.tags_prompt" /></option>
                            <c:choose>
                                <c:when test="${empty news}">
                                    <c:forEach var="tag" items="${tags}">
                                        <option value="${tag.tagId}">${tag.tagName}</option>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="tag" items="${tags}">
                                        <option value="${tag.tagId}" ${newsTags.contains(tag) ? 'selected' : ''}>${tag.tagName}</option>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </select>
                    </div>
                    <div id="save-news-button-wrapper">
                        <button id="save-news-button"><spring:message code="news_edit.save" /></button>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
        <script type="text/javascript" src="<c:url value="/resources/js/news-edit.js" />"></script>
    </body>
</html>
