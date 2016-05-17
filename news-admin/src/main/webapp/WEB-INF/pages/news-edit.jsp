<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
    <head>
        <meta charset="utf-8">

        <title>News Management | News</title>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/jquery.multiselect.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/assets/style.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/assets/prettify.css" />" rel="stylesheet">
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" rel="stylesheet">

        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/jquery.multiselect.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/assets/prettify.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/news-list-administration.js" />"></script>
    </head>
    <body>
        <div id="container">
            <tiles:insertAttribute name="header" />
            <div id="crutch">
                <tiles:insertAttribute name="navbar" />
                <div id="content" class="scrollable news-content">
                    <div id="new-news">
                        <div id="new-news-title-row">
                            <div id="new-news-title-label">
                                Title:
                            </div>
                            <div id="new-news-title">
                                <input id="new-news-textarea" type="text" value="">
                            </div>
                        </div>
                        <div id="date-row">
                            <div id="date-title">
                                Date:
                            </div>
                            <div id="date">
                                <input id="date-textarea" type="text" value="">
                            </div>
                        </div>
                        <div id="short-text-row">
                            <div id="short-text-title">
                                Brief:
                            </div>
                            <div id="short-text">
                                <textarea id="short-text-textarea" rows="4" >
                                </textarea>
                            </div>
                        </div>
                        <div id="text-row">
                            <div id="text-title">
                                Content:
                            </div>
                            <div id="text">
                                <textarea id="text-textarea" rows="6" >
                                </textarea>
                            </div>
                        </div>
                    </div>
                    <div id="filter-row">
                        <select id="authors" name="authors">
                            <option value="" disabled selected>Please select the author</option>
                            <option value="Chris Kahn">Chris Kahn</option>
                            <option value="Jeff Mason">Jeff Mason</option>
                            <option value="Ginger Gibson">Ginger Gibson</option>
                            <option value="My Pham">My Pham</option>
                            <option value="David Lawder">David Lawder</option>
                            <option value="Lindsay Dunsmuird">Lindsay Dunsmuird</option>
                        </select>

                        <select id="tags" multiple="multiple" name="tags">
                            <option value="" disabled>Please select the tag</option>
                            <option value="politics">politics</option>
                            <option value="election">election</option>
                            <option value="republican">republican</option>
                            <option value="Donald Trump">Donald Trump</option>
                            <option value="White House">White House</option>
                            <option value="Barack Obama">Barack Obama</option>
                            <option value="president">president</option>
                            <option value="Vietnam">Vietnam</option>
                        </select>
                    </div>
                    <div id="save-news-button-wrapper">
                        <button id="save-news-button">Save</button>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>
