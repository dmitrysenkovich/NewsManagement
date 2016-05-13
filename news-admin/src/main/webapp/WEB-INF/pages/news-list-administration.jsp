<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
    <head>
        <meta charset="utf-8">

        <title>News Management | News</title>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/jquery.multiselect.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/assets/style.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/assets/prettify.css" />" rel="stylesheet">
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" rel="stylesheet">

        <script type="text/javascript" src="<c:url value="/resources/js/jquery.multiselect.min.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/assets/prettify.js" />"></script>
        <script type="text/javascript" src="<c:url value="/resources/js/newsmanagement.js" />"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.min.js"></script>
    </head>
    <body>
        <div id="container">
            <tiles:insertAttribute name="header" />
            <div id="crutch">
                <tiles:insertAttribute name="navbar" />
                <div id="content" class="scrollable">
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

                        <button id="filter-button">Filter</button>
                        <button id="reset-button">Reset</button>
                    </div>
                    <div id="news-list">
                        <div class="short-news">
                            <div class="short-news-title-row">
                                <div class="short-news-title">
                                    <a href="#">
                                        Exclusive: Top reason Americans will vote for Trump: 'To stop Clinton' - poll
                                    </a>
                                </div>
                                <div class="short-news-authors">
                                    (by Chris Kahn)
                                </div>
                                <div class="short-news-last-edit">
                                    <u>9, 2016 6:59</u>
                                </div>
                            </div>
                            <div class="short-news-short-text-row">
                                NEW YORK The U.S. presidential election may turn out to be one of the world's biggest un-popularity contests.
                            </div>
                            <div class="short-news-footer-row">
                                <div class="short-news-tags">
                                    Politics, election
                                </div>
                                <div class="short-news-others">
                                    <span style="color: #ff0000">Comments(5)</span> <a href="#">Edit</a> <input type="checkbox" />
                                </div>
                            </div>
                        </div>
                        <div class="short-news">
                            <div class="short-news-title-row">
                                <div class="short-news-title">
                                    <a href="#">
                                        'This is not a reality show,' Obama tells Trump
                                    </a>
                                </div>
                                <div class="short-news-authors">
                                    (by Jeff Mason, Ginger Gibson)
                                </div>
                                <div class="short-news-last-edit">
                                    <u>9, 2016 6:59</u>
                                </div>
                            </div>
                            <div class="short-news-short-text-row">
                                WASHINGTON U.S. President Barack Obama warned on Friday that occupying the Oval Office "is not a reality show," in a swipe at outspoken Republican candidate Donald Trump who is vying to replace him in the White House.
                            </div>
                            <div class="short-news-footer-row">
                                <div class="short-news-tags">
                                    Republican, Donald Trump, White House
                                </div>
                                <div class="short-news-others">
                                    <span style="color: #ff0000">Comments(15)</span> <a href="#">Edit</a> <input type="checkbox" />
                                </div>
                            </div>
                        </div>
                        <div class="short-news">
                            <div class="short-news-title-row">
                                <div class="short-news-title">
                                    <a href="#">
                                        Top U.S. official visits Vietnam to assess human rights progress
                                    </a>
                                </div>
                                <div class="short-news-authors">
                                    (by My Pham)
                                </div>
                                <div class="short-news-last-edit">
                                    <u>9, 2016 6:59</u>
                                </div>
                            </div>
                            <div class="short-news-short-text-row">
                                HANOI A top U.S. envoy began a two-day trip to Vietnam on Monday to gauge its progress in human rights, two weeks ahead of a visit by President Barack Obama in what will be the first by a U.S. leader in a decade.
                            </div>
                            <div class="short-news-footer-row">
                                <div class="short-news-tags">
                                    Barack Obama, president, Vietnam
                                </div>
                                <div class="short-news-others">
                                    <span style="color: #ff0000">Comments(84)</span> <a href="#">Edit</a> <input type="checkbox" />
                                </div>
                            </div>
                        </div>
                        <div class="short-news">
                            <div class="short-news-title-row">
                                <div class="short-news-title">
                                    <a href="#">
                                        Trump changes tune on tax hikes for wealthy Americans
                                    </a>
                                </div>
                                <div class="short-news-authors">
                                    (by David Lawder, Lindsay Dunsmuird)
                                </div>
                                <div class="short-news-last-edit">
                                    <u>9, 2016 6:59</u>
                                </div>
                            </div>
                            <div class="short-news-short-text-row">
                                WASHINGTON U.S. Republican presidential candidate Donald Trump said on Sunday he was open to raising taxes on the rich, backing off his prior proposal to reduce taxes on all Americans and breaking with one of his party's core policies dating back to the 1990s.
                            </div>
                            <div class="short-news-footer-row">
                                <div class="short-news-tags">
                                    Barack Obama, president, Vietnam
                                </div>
                                <div class="short-news-others">
                                    <span style="color: #ff0000">Comments(46)</span> <a href="#">Edit</a> <input type="checkbox" />
                                </div>
                            </div>
                        </div>
                        <button id="delete-button">Delete</button>
                    </div>
                    <div id="pagination-row">
                        <ul class="pagination">
                            <li><a id="first-page" href="#">«</a></li>
                            <li><a id="previous-page" href="#">❮</a></li>
                            <li><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a class="active" href="#">4</a></li>
                            <li><a href="#">5</a></li>
                            <li><a href="#">6</a></li>
                            <li><a href="#">7</a></li>
                            <li><a id="next-page" href="#">❯</a></li>
                            <li><a id="last-page" href="#">»</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>
