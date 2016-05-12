<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
    <head>
        <meta charset="utf-8">

        <title>News Management | Add Tags</title>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
    </head>
    <body>
        <div id="container">
            <tiles:insertAttribute name="header" />
            <div id="crutch">
                <tiles:insertAttribute name="navbar" />
                <div id="content" class="scrollable news-content">
                    <div id="items-list">
                        <div class="item">
                            <div class="item-label">Tag:</div>
                            <div class="item-name"><input class="item-name-textarea" type="text" value="politics" disabled></div>
                            <div class="item-action">
                                <div class="item-update-links" hidden>
                                    <a href="#"><u>update</u></a>
                                    <a href="#"><u>delete</u></a>
                                    <a href="#"><u>cancel</u></a>
                                </div>
                                <a href="#"><u>edit</u></a>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-label">Tag:</div>
                            <div class="item-name"><input class="item-name-textarea" type="text" value="election" disabled></div>
                            <div class="item-action">
                                <div class="item-update-links" hidden>
                                    <a href="#"><u>update</u></a>
                                    <a href="#"><u>delete</u></a>
                                    <a href="#"><u>cancel</u></a>
                                </div>
                                <a href="#"><u>edit</u></a>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-label">Tag:</div>
                            <div class="item-name"><input class="item-name-textarea" type="text" value="republican" disabled></div>
                            <div class="item-action">
                                <div class="item-update-links" hidden>
                                    <a href="#"><u>update</u></a>
                                    <a href="#"><u>delete</u></a>
                                    <a href="#"><u>cancel</u></a>
                                </div>
                                <a href="#"><u>edit</u></a>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-label">Tag:</div>
                            <div class="item-name"><input class="item-name-textarea" type="text" value="Donald Trump" disabled></div>
                            <div class="item-action">
                                <div class="item-update-links" hidden>
                                    <a href="#"><u>update</u></a>
                                    <a href="#"><u>delete</u></a>
                                    <a href="#"><u>cancel</u></a>
                                </div>
                                <a href="#"><u>edit</u></a>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-label">Tag:</div>
                            <div class="item-name"><input class="item-name-textarea" type="text" value="president" disabled></div>
                            <div class="item-action">
                                <div class="item-update-links" hidden>
                                    <a href="#"><u>update</u></a>
                                    <a href="#"><u>delete</u></a>
                                    <a href="#"><u>cancel</u></a>
                                </div>
                                <a href="#"><u>edit</u></a>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-label">Tag:</div>
                            <div class="item-name"><input class="item-name-textarea" type="text" value="Vietnam" disabled></div>
                            <div class="item-action">
                                <div class="item-update-links" hidden>
                                    <a href="#"><u>update</u></a>
                                    <a href="#"><u>delete</u></a>
                                    <a href="#"><u>cancel</u></a>
                                </div>
                                <a href="#"><u>edit</u></a>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-label">Tag:</div>
                            <div class="item-name"><input class="item-name-textarea" type="text" value="White House"></div>
                            <div class="item-action">
                                <div class="item-update-links">
                                    <a href="#"><u>update</u></a>
                                    <a href="#"><u>delete</u></a>
                                    <a href="#"><u>cancel</u></a>
                                </div>
                                <a href="#" hidden><u>edit</u></a>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-label">Tag:</div>
                            <div class="item-name"><input class="item-name-textarea" type="text" value="Barack Obama" disabled></div>
                            <div class="item-action">
                                <div class="item-update-links" hidden>
                                    <a href="#"><u>update</u></a>
                                    <a href="#"><u>delete</u></a>
                                    <a href="#"><u>cancel</u></a>
                                </div>
                                <a href="#"><u>edit</u></a>
                            </div>
                        </div>
                    </div>
                    <div id="add-item">
                        <div class="item-label">Add Tag:</div>
                        <div class="item-name"><input id="new-item-name-textarea" type="text" value="" disabled></div>
                        <div id="save-item-link">
                            <a href="#"><u>save</u></a>
                        </div>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>
