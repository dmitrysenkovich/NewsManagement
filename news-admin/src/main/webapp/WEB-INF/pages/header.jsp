<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div id="header">
    <h1 id="title"><spring:message code="header.header" /></h1>
    <c:url value="/j_spring_security_logout" var="logoutUrl" />
    <form id="greeting" action="${logoutUrl}" method="post">
        <spring:message code="header.greeting" /> ${userName}
        <span>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <spring:message code="header.logout_button" var="logout"/>
            <input id="logout-button" type="submit" value="${logout}">
        </span>
    </form>
    <div id="language">
        <a href="?language=en"><spring:message code="header.english" /></a>
        <a href="?language=ru_RU"><spring:message code="header.russian" /></a>
    </div>
</div>