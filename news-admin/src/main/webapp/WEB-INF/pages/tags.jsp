<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <meta charset="utf-8">

        <title><spring:message code="tags.header" /></title>

        <sec:csrfMetaTags/>
        <c:set var="localeCode" value="${pageContext.response.locale}" />
        <script>var localeCode = '${localeCode}';</script>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">

        <script type="text/javascript" src="<c:url value="/resources/js/jquery-2.0.0.min.js" />"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/jquery.i18n.properties.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/localization.js" />"></script>
    </head>
    <body>
        <div id="container">
            <tiles:insertAttribute name="header" />
            <div id="crutch">
                <tiles:insertAttribute name="navbar" />
                <div id="content" class="scrollable news-content">
                    <div id="items-list">
                        <c:forEach var="tag" items="${tags}">
                            <div id="${tag.tagId}" class="item">
                                <div class="item-label"><spring:message code="tags.tag" />:</div>
                                <div class="item-name"><input class="item-name-textarea" type="text" value="${tag.tagName}" disabled></div>
                                <div class="item-action">
                                    <div class="item-update-links" hidden>
                                        <a href="javascript:void(0)" class="update-tag-link"><u><spring:message code="tags.update" /></u></a>
                                        <a href="javascript:void(0)" class="delete-tag-link"><u><spring:message code="tags.delete" /></u></a>
                                        <a href="javascript:void(0)" class="cancel-tag-link"><u><spring:message code="tags.cancel" /></u></a>
                                    </div>
                                    <a href="javascript:void(0)" class="edit-tag-link"><u><spring:message code="tags.edit" /></u></a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div id="add-item">
                        <div class="item-label"><spring:message code="tags.add_tag" />:</div>
                        <div class="item-name"><input id="new-item-name-textarea" type="text" value=""></div>
                        <div id="save-item-link">
                            <a href="javascript:void(0)" class="add-tag-link"><u><spring:message code="tags.save" /></u></a>
                        </div>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
        <script type="text/javascript" src="<c:url value="/resources/js/tags.js" />"></script>
    </body>
</html>
