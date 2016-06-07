<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div id="navbar">
    <div id="links">
        <ul>
            <li><a id="news-list-link" href="/news-admin/"><spring:message code="navbar.news_list" /></a></li>
            <li><a id="add-news-link" href="/news-admin/add"><spring:message code="navbar.add_news" /></a></li>
            <li><a id="authors-link" href="/news-admin/authors"><spring:message code="navbar.authors" /></a></li>
            <li><a id="tags-link" href="/news-admin/tags"><spring:message code="navbar.tags" /></a></li>
        </ul>
    </div>
</div>