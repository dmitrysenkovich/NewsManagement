<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="header">
    <h1 id="title">News portal - Administration</h1>
    <c:url value="/j_spring_security_logout" var="logoutUrl" />
    <form id="greeting" action="${logoutUrl}" method="post">
        Hello, admin ${userName}
        <span>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input id="logout-button" type="submit" value="Logout">
        </span>
    </form>
    <div id="language">
        <a href="#">EN</a>
        <a href="#">RU</a>
    </div>
</div>