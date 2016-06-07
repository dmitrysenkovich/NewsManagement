<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <meta charset="utf-8">

        <title>News Management | Login</title>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
    </head>
    <body>
        <div id="container">
            <tiles:insertAttribute name="header" />
            <div id="crutch">
                <div id="user-content">
                    <form id="login-form" name="loginForm" action="<c:url value='/j_spring_security_check' />" method='POST'>
                        <c:if test="${not empty error}">
                            <div id="error-message-login"><spring:message code="login.error" /></div>
                        </c:if>
                        <c:if test="${not empty logout}">
                            <div id="message-login"><spring:message code="login.logout" /></div>
                        </c:if>
                        <div class="login-row">
                            <div class="label"><spring:message code="login.login" />:</div><input id="login" name="login" class="login-form-input" type="text"><br>
                        </div>
                        <div class="login-row">
                            <div class="label"><spring:message code="login.password" />:</div><input id="password" name="password" class="login-form-input" type="password"><br>
                        </div>
                        <button id="login-button" class="row-padded" type="submit"><spring:message code="login.login" /></button>
                        <input type="hidden" name="${_csrf.parameterName}"
                               value="${_csrf.token}" />
                    </form>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>
