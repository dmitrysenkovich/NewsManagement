<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <meta charset="utf-8">

        <title>News Management | Add Authors</title>

        <sec:csrfMetaTags/>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">

        <script type="text/javascript" src="<c:url value="/resources/js/jquery-2.0.0.min.js" />"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.min.js"></script>
    </head>
    <body>
        <div id="container">
            <tiles:insertAttribute name="header" />
            <div id="crutch">
                <tiles:insertAttribute name="navbar" />
                <div id="content" class="scrollable news-content">
                    <div id="items-list">
                        <c:forEach var="author" items="${authors}">
                            <div id="${author.authorId}" class="item">
                                <div class="item-label">Author:</div>
                                <div class="item-name"><input class="item-name-textarea" type="text" value="${author.authorName}" disabled></div>
                                <div class="item-action">
                                    <div class="item-update-links" hidden>
                                        <a href="javascript:void(0)" class="update-author-link"><u>update</u></a>
                                        <c:if test="${empty author.expired}">
                                            <a href="javascript:void(0)" class="expire-author-link"><u>expire</u></a>
                                        </c:if>
                                        <a href="javascript:void(0)" class="cancel-author-link"><u>cancel</u></a>
                                    </div>
                                    <a href="javascript:void(0)" class="edit-author-link"><u>edit</u></a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div id="add-item">
                        <div class="item-label">Add Author:</div>
                        <div class="item-name"><input id="new-item-name-textarea" type="text" value=""></div>
                        <div id="save-item-link">
                            <a href="javascript:void(0)" class="add-author-link"><u>save</u></a>
                        </div>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
        <script type="text/javascript" src="<c:url value="/resources/js/authors.js" />"></script>
    </body>
</html>
