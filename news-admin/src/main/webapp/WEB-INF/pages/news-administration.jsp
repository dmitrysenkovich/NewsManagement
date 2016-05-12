<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
    <head>
        <meta charset="utf-8">

        <title>News Management | News</title>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
    </head>
    <body>
        <div id="container">
            <tiles:insertAttribute name="header" />
            <div id="crutch">
                <tiles:insertAttribute name="navbar" />
                <div id="content" class="scrollable news-content">
                    <div class="news">
                        <div class="title">
                            Exclusive: Top reason Americans will vote for Trump: 'To stop Clinton' - poll
                        </div>
                        <div class="data-and-authors-row">
                            <div class="news-authors">
                                (by Chris Kahn)
                            </div>
                            <div class="news-last-edit">
                                <u>9, 2016 6:59</u>
                            </div>
                        </div>
                        <div class="news-text">
                            The U.S. presidential election may turn out to be one of the world's biggest un-popularity contests.<br>
                            Nearly half of American voters who support either Democrat Hillary Clinton or Republican Donald Trump for the White House said they will mainly be trying to block the other side from winning, according to a Reuters/Ipsos poll released Thursday.<br>
                            The results reflect a deepening ideological divide in the United States, where people are becoming increasingly fearful of the opposing party, a feeling worsened by the likely matchup between the New York real estate tycoon and the former first lady, said Larry Sabato, director of the University of Virginia’s Center for Politics.<br>
                            "This phenomenon is called negative partisanship," Sabato said. "If we were trying to maximize the effect, we couldn't have found better nominees than Trump and Clinton."<br>
                            Trump has won passionate supporters and vitriolic detractors for his blunt talk and hardline proposals, including his call for a ban on Muslims entering the United States, his vow to force Mexico to pay for a border wall, and his promise to renegotiate international trade deals.
                        </div>
                        <div id="comments">
                            <div class="comment">
                                <div class="comment-date">
                                    <u>9, 2016 6:59</u>
                                </div>
                                <div class="comment-text-wrapper">
                                    <span class="delete-post-button-span"><button class="delete-post-button">&times;</button></span>
                                    <div class="comment-text">
                                        I know you're probably joking but why is it the way it is? And how do we fix it
                                    </div>
                                </div>
                            </div>
                            <div class="comment">
                                <div class="comment-date">
                                    <u>9, 2016 6:59</u>
                                </div>
                                <div class="comment-text-wrapper">
                                    <span class="delete-post-button-span"><button class="delete-post-button">&times;</button></span>
                                    <div class="comment-text">
                                        Look at this fucking moderate over here making reasoned choices based on his personal political beliefs and not bowing to peer pressure.
                                    </div>
                                </div>
                            </div>
                            <div class="comment">
                                <div class="comment-date">
                                    <u>9, 2016 6:59</u>
                                </div>
                                <div class="comment-text-wrapper">
                                    <span class="delete-post-button-span"><button class="delete-post-button">&times;</button></span>
                                    <div class="comment-text">
                                        As a complement to early devoted users fading away, even if they stay, there's a big dilution factor of people who (on average--of course some new thoughtful contributors do keep come in with the tide) made less effort to find the sub, care less about quality of comments, and upvote a higher percentage of quick browse fodder (images, short comments). Right now there are some pretty good small politics subs that have good conversation in them, but I won't link them here, because reasons.
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="new-comment-text">
                            <textarea id="new-post-textarea" rows="4" >
                            </textarea>
                        </div>
                        <div id="post-comment-button-wrapper">
                            <button id="post-comment-button">Post comment</button>
                        </div>
                    </div>
                    <div id="news-footer">
                        <div id="previous-link">
                            <a href="#"><u>PREVIOUS</u></a>
                        </div>
                        <div id="next-link">
                            <a href="#"><u>NEXT</u></a>
                        </div>
                    </div>
                </div>
            </div>
            <tiles:insertAttribute name="footer" />
        </div>
    </body>
</html>
