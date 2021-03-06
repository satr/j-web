<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Account</title>
    <link href="/css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="page">
    <%@include file="/WEB-INF/common/Header.jsp" %>

    <h3>Login</h3>
    <div>
        <jsp:include page="/WEB-INF/common/Errors.jsp" />
        <form action="/login" method="post">
            <div>
                <table class="product">
                    <tr><td>Email</td>
                        <td><input type="text" name="email" /></td>
                    </tr>
                    <tr><td>Password</td>
                        <td><input type="password" name="password" /></td>
                    </tr>
                </table>
            </div>
            <br />
            <div>
                <input type="submit" value="Login">
                <input type="button" value="Cancel" onclick="window.location = '/'">
            </div>
        </form>
    </div>
</div>

<%@include file="/WEB-INF/common/Footer.jsp" %>
</body>
</html>
