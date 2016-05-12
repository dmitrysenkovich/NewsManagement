<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <meta charset="utf-8">

        <title>News Management | Login</title>

        <link rel="stylesheet" href="./css/style.css">
        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
    </head>
    <body>
        <div id="container">
            <div id="header" class="bordered">
                <h1 id="title">News portal - Administration</h1>
                <div id="language">
                    <a href="#">EN</a>
                    <a href="#">RU</a>
                </div>
            </div>
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
            <div id="footer">
               Copyright @ Epam 2016. All rights reserved.
            </div>
        </div>
    </body>
</html>
