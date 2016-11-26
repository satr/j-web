<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Account</title>
    <link href="/css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="page">
    <%@include file="/WEB-INF/common/Header.jsp" %>
    <c:choose>
        <c:when test='${account.isEditAction()}'>
            <h3>Edit Account</h3>
        </c:when>
        <c:otherwise>
            <h3>Sign Up</h3>
        </c:otherwise>
    </c:choose>
    <form action="/account/edit" method="post">
        <input type="hidden" name="action" value="${account.getAction()}">
        <div>
            <jsp:include page="/WEB-INF/common/Errors.jsp" />
            <table class="account">
                <tr><td>First Name</td>
                    <td><input type="text" name="firstName" value="${account.getFirstName()}" /></td>
                </tr>
                <tr><td>Middle Name</td>
                    <td><input type="text" name="middleName" value="${account.getMiddleName()}" /></td>
                </tr>
                <tr><td>Last Name</td>
                    <td><input type="text" name="lastName" value="${account.getLastName()}" /></td>
                </tr>
                <tr><td>Email</td>
                    <td><input type="email" name="email" value="${account.getEmail()}" /></td>
                </tr>
                <c:choose>
                    <c:when test='${account.isEditAction()}'>
                        <tr>
                            <td>Repeat New Email</td>
                            <td><input type="text" name="repeatedEmail" value="${account.getRepeatedEmail()}" /></td>
                        </tr>
                        <tr><td>Current Password</td>
                            <td><input type="password" name="currentPassword" /></td>
                        </tr>
                        <tr><td>New Password</td>
                            <td><input type="password" name="newPassword" /></td>
                        </tr>
                        <tr><td>Repeat New Password</td>
                            <td><input type="password" name="repeatedPassword" /></td>
                        </tr>
                        <tr><td>Created</td>
                            <td>${account.getCreatedOn()}</td>
                        </tr>
                        <tr><td>Last Updated</td>
                            <td>${account.getUpdatedOn()}</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td>Repeat Email</td>
                            <td><input type="text" name="repeatedEmail" value="${account.getRepeatedEmail()}"  /></td>
                        </tr>
                        <tr><td>Password</td>
                            <td><input type="password" name="newPassword" /></td>
                        </tr>
                        <tr><td>Repeat Password</td>
                            <td><input type="password" name="repeatedPassword" /></td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </table>
        </div>
        <br />
        <div>
            <input type="submit" value="Save">
            <input type="button" value="Cancel" onclick="window.location = '/account/detail'">
        </div>
    </form>
</div>

<%@include file="/WEB-INF/common/Footer.jsp" %>
</body>
</html>
