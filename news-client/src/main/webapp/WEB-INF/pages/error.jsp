<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
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

        <title><spring:message code="error.header" /></title>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet" />

        <script src="<c:url value="/resources/js/jquery-2.0.0.min.js" />"></script>
    </head>
    <body>
        <div id="container">
            <tiles:insertAttribute name="header" />
            <div id="crutch">
                <div id="user-content">
                    <div id="error-div">
                        <div>${errorMessage}</div>
                        <button id="back-button" onclick="history.go(-1);"><spring:message code="error.back" /></button>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>