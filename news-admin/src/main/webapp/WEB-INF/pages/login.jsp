<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
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
                    <div id="login-form">
                        <div class="login-row">
                            <div class="label">Login:</div><input id="login" class="login-form-input" type="text"><br>
                        </div>
                        <div class="login-row">
                            <div class="label">Password:</div><input id="password" class="login-form-input" type="text"><br>
                        </div>
                        <button id="login-button" class="row-padded" type="button">Login</button>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>
