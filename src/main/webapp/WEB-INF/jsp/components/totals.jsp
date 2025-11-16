<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="totalIncome" value="${requestScope.totalIncome}" />
<c:if test="${empty totalIncome}">
    <c:set var="totalIncome" value="<%= java.math.BigDecimal.ZERO %>" />
</c:if>
<c:set var="totalExpense" value="${requestScope.totalExpense}" />
<c:if test="${empty totalExpense}">
    <c:set var="totalExpense" value="<%= java.math.BigDecimal.ZERO %>" />
</c:if>
<c:set var="totalBalance" value="${requestScope.totalBalance}" />
<c:if test="${empty totalBalance}">
    <c:set var="totalBalance" value="<%= java.math.BigDecimal.ZERO %>" />
</c:if>

<div class="total-summary">
    <div class="total-item total-income">
        <h3><i class="fas fa-arrow-up"></i> Общие доходы</h3>
        <p class="income-amount">${totalIncome} ₽</p>
    </div>
    <div class="total-item total-expense">
        <h3><i class="fas fa-arrow-down"></i> Общие расходы</h3>
        <p class="expense-amount">${totalExpense} ₽</p>
    </div>
    <div class="total-item total-balance">
        <h3><i class="fas fa-balance-scale"></i> Баланс</h3>
        <p class="balance-${totalBalance > 0 ? 'positive' : totalBalance < 0 ? 'negative' : 'zero'}">
            ${totalBalance} ₽
        </p>
    </div>
</div>