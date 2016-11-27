<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Cart</title>
    <link href="/css/style.css" rel="stylesheet" type="text/css" />
    <script language="JavaScript">
        function removeItem(button, productId) {
            document.f.targetProductId.value = productId;
            button.submit();
        }
    </script>
</head>
<body>
<div class="page">
<%@include file="/WEB-INF/common/Header.jsp" %>
<h3>Cart</h3>
    <form name="f" action="/cart/update" method="post">
        <c:choose>
            <c:when test="${cartItems.size() > 0}">
                <input type="hidden" name="targetProductId" />
                <table class="product">
                    <tr><th>SKU</th><th>Name</th><th>Price</th><th>Amount</th><th>&nbsp;</th></tr>
                    <c:forEach items="${cartItems}" var="cartItem">
                        <tr>
                            <td>${cartItem.getProduct().getId()}<input type="hidden" name="productId" value="${cartItem.getProduct().getId()}"></td>
                            <td><a href='<c:url value="/product/detail?id=${cartItem.getProduct().getId()}"></c:url>'>${cartItem.getProduct().getName()}</a></td>
                            <td>${cartItem.getPrice()}</td>
                            <td class="num"><input type="number" min="1" placeholder="0" name="amount" value="${cartItem.getAmount()}" /></td>
                            <td><input type="submit" name="action" value="Remove" onclick="removeItem(this, ${cartItem.getProduct().getId()})" /></td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="3">&nbsp;</td><td><input type="submit" name="action" value="Update" /></td><td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td>Sum</td>
                        <td><fmt:formatNumber minIntegerDigits="1" maxFractionDigits="2" minFractionDigits="2">${sum}</fmt:formatNumber></td>
                        <td>&nbsp;</td><td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td>Discount</td>
                        <td><fmt:formatNumber minIntegerDigits="1" maxFractionDigits="2" minFractionDigits="2">${discount}</fmt:formatNumber></td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td>Total</td>
                        <td><fmt:formatNumber minIntegerDigits="1" maxFractionDigits="2" minFractionDigits="2">${total}</fmt:formatNumber></td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                </table>
                <br />
                <div>
                    <input type="button" value="Continue Shopping" onclick="window.location = '/products'" />
                    <input type="submit" name="action" value="Proceed" />&nbsp;
                </div>
            </c:when>
            <c:otherwise>
                <h5>Cart is empty.</h5>
                <input type="button" value="Continue Shopping" onclick="window.location = '/products'" />
            </c:otherwise>
        </c:choose>
    </form>
</div>
<%@include file="/WEB-INF/common/Footer.jsp" %>

</body>
</html>
