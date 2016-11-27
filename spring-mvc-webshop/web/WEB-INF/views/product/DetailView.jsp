<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Product Detail</title>
    <link href="/css/style.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="page">
    <%@include file="/WEB-INF/common/Header.jsp" %>

    <h3>Product</h3>
        <div>
            <table class="product">
                <c:if test="${product.getId() != 0}">
                    <tr>
                        <td>SKU</td>
                        <td>${product.getId()}</td>
                    </tr>
                </c:if>
                <tr>
                    <td>Name</td>
                    <td>${product.getName()}</td>
                </tr>
                <tr>
                    <td>Price</td>
                    <td><fmt:formatNumber minIntegerDigits="1" maxFractionDigits="2"
                                          minFractionDigits="2">${product.getPrice()}</fmt:formatNumber></td>
                </tr>
                <tr>
                    <td>Amount</td>
                    <td><fmt:formatNumber value="${product.getStock().getAmount()}" minIntegerDigits="1" maxFractionDigits="0"
                                          currencyCode=""></fmt:formatNumber></td>
                </tr>
            </table>
        </div>
        <br/>
        <div>
            <input type="button" value="Edit" onclick="window.location = '/product/edit/?id=${product.getId()}'">
            <input type="button" value="Buy" onclick="window.location = '/buy?sku=${product.getId()}'">
            <input type="button" value="List" onclick="window.location = '/products'">
        </div>
</div>

<%@include file="/WEB-INF/common/Footer.jsp" %>
</body>
</html>
