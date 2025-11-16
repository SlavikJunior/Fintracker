<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="java.util.List,com.slavikjunior.models.*,java.text.SimpleDateFormat,java.math.BigDecimal,java.util.*" %>
<c:set var="transactionGroups" value="${requestScope.transactionGroups}" />
<c:if test="${empty transactionGroups}">
    <c:set var="transactionGroups" value="<%= Collections.emptyList() %>" />
</c:if>

<c:set var="totalIncome" value="${requestScope.totalIncome}" />
<c:if test="${empty totalIncome}">
    <c:set var="totalIncome" value="<%= BigDecimal.ZERO %>" />
</c:if>

<c:set var="totalExpense" value="${requestScope.totalExpense}" />
<c:if test="${empty totalExpense}">
    <c:set var="totalExpense" value="<%= BigDecimal.ZERO %>" />
</c:if>

<c:set var="totalBalance" value="${requestScope.totalBalance}" />
<c:if test="${empty totalBalance}">
    <c:set var="totalBalance" value="<%= BigDecimal.ZERO %>" />
</c:if>

<c:set var="incomeCategories" value="${requestScope.incomeCategories}" />
<c:if test="${empty incomeCategories}">
    <c:set var="incomeCategories" value="<%= Collections.emptyList() %>" />
</c:if>

<c:set var="expenseCategories" value="${requestScope.expenseCategories}" />
<c:if test="${empty expenseCategories}">
    <c:set var="expenseCategories" value="<%= Collections.emptyList() %>" />
</c:if>

<c:set var="allCategories" value="${requestScope.allCategories}" />
<c:if test="${empty allCategories}">
    <c:set var="allCategories" value="<%= Collections.emptyList() %>" />
</c:if>

<c:set var="userTags" value="${requestScope.userTags}" />
<c:if test="${empty userTags}">
    <c:set var="userTags" value="<%= Collections.emptyList() %>" />
</c:if>

<c:set var="dateFormat" value="<%= new SimpleDateFormat(\"dd.MM.yyyy HH:mm\") %>" />
<c:set var="dayFormat" value="<%= new SimpleDateFormat(\"dd.MM.yyyy\") %>" />
<c:set var="monthFormat" value="<%= new SimpleDateFormat(\"MMMM yyyy\", new Locale(\"ru\")) %>" />

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FinTracker - Главная</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<jsp:include page="components/header.jsp"/>

<jsp:include page="components/messages.jsp"/>

<div class="container">
    <section class="add-transaction">
        <jsp:include page="components/add-transaction.jsp"/>
        <jsp:include page="components/tag-management.jsp"/>
    </section>

    <section class="transactions">
        <jsp:include page="components/totals.jsp"/>
        <jsp:include page="components/filters.jsp"/>
        <jsp:include page="components/transactions.jsp"/>
    </section>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
<jsp:include page="components/tag-modal.jsp"/>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>