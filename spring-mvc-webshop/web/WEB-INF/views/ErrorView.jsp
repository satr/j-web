<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
    <link href="/css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<h2>Error</h2>
<div>
<%@include file="/WEB-INF/common/Header.jsp" %>
    <jsp:include page="../common/Errors.jsp" />
    <input type="button" value="Back" onclick="history.back()">
</div>
<%@include file="/WEB-INF/common/Footer.jsp" %>
</body>
</html>
