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
                            <div id="error-message-login">Invalid login or password</div>
                        </c:if>
                        <c:if test="${not empty logout}">
                            <div id="message-login">Successfully logged out</div>
                        </c:if>
                        <div class="login-row">
                            <div class="label">Login:</div><input id="login" name="login" class="login-form-input" type="text"><br>
                        </div>
                        <div class="login-row">
                            <div class="label">Password:</div><input id="password" name="password" class="login-form-input" type="password"><br>
                        </div>
                        <button id="login-button" class="row-padded" type="submit">Login</button>
                        <input type="hidden" name="${_csrf.parameterName}"
                               value="${_csrf.token}" />
                    </form>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>
