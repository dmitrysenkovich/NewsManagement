<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <meta charset="utf-8">

        <title><spring:message code="news.header" /></title>

        <sec:csrfMetaTags/>
        <c:set var="localeCode" value="${pageContext.response.locale}" />
        <script>var localeCode = '${localeCode}';</script>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">

        <script type="text/javascript" src="<c:url value="/resources/js/jquery-2.0.0.min.js" />"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/localization.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/news.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/jquery.i18n.properties.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/localization.js" />"></script>
    </head>
    <body>
        <div id="container">
            <tiles:insertAttribute name="header" />
            <div id="crutch">
                <tiles:insertAttribute name="navbar" />
                <div id="content" class="scrollable news-content">
                    <div class="news">
                        <div class="title">
                            ${news.title}
                        </div>
                        <div class="data-and-authors-row">
                            <div class="news-authors">
                                <c:set var="authorsNames" value="" />
                                <c:forEach var="author" items="${authors}" varStatus="loopStatus">
                                    <c:choose>
                                        <c:when test="${loopStatus.first}">
                                            <c:set var="authorsNames" value="${authorsNames}${author.authorName}" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="authorsNames" value="${authorsNames}, ${author.authorName}" />
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                (<spring:message code="news.by" /> ${authorsNames})
                            </div>
                            <div class="news-last-edit">
                                <u><fmt:formatDate
                                        value="${news.modificationDate}"
                                        dateStyle="long" />
                                </u>
                            </div>
                        </div>
                        <div class="news-text">
                            ${news.fullText.replace('\\n', '<br>')}
                        </div>
                        <div id="comments">
                            <c:forEach var="comment" items="${comments}">
                                <div class="comment">
                                    <div class="comment-date">
                                        <u><fmt:formatDate
                                                value="${comment.creationDate}"
                                                dateStyle="long" />
                                        </u>
                                    </div>
                                    <div class="comment-text-wrapper">
                                        <span class="delete-comment-button-span"><button id="${comment.commentId}" class="delete-comment-button">&times;</button></span>
                                        <div class="comment-text">
                                            ${comment.commentText.replace('\\n', '<br>')}
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div id="new-comment">
                        <div id="new-comment-text">
                            <textarea id="new-post-textarea" rows="4" ></textarea>
                        </div>
                        <div id="post-comment-button-wrapper">
                            <button id="post-comment-button"><spring:message code="news.comment" /></button>
                        </div>
                    </div>
                    <div id="news-footer">
                        <div id="previous-link">
                            <a id="previous" href="javascript:void(0)"
                                class="${not empty first ? 'disabled-page-link' : ''}"><u><spring:message code="news.previous" /></u></a>
                        </div>
                        <div id="next-link">
                            <a id="next" href="javascript:void(0)"
                                class="${not empty last ? 'disabled-page-link' : ''}"><u><spring:message code="news.next" /></u></a>
                        </div>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>
