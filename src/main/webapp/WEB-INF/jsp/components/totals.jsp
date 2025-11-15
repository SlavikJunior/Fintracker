<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.math.BigDecimal" %>
<%
    BigDecimal totalIncome = (BigDecimal) request.getAttribute("totalIncome");
    BigDecimal totalExpense = (BigDecimal) request.getAttribute("totalExpense");
    BigDecimal totalBalance = (BigDecimal) request.getAttribute("totalBalance");

    if (totalIncome == null) totalIncome = BigDecimal.ZERO;
    if (totalExpense == null) totalExpense = BigDecimal.ZERO;
    if (totalBalance == null) totalBalance = BigDecimal.ZERO;
%>

<div class="total-summary">
    <div class="total-item total-income">
        <h3><i class="fas fa-arrow-up"></i> Общие доходы</h3>
        <p class="income-amount"><%= totalIncome %> ₽</p>
    </div>
    <div class="total-item total-expense">
        <h3><i class="fas fa-arrow-down"></i> Общие расходы</h3>
        <p class="expense-amount"><%= totalExpense %> ₽</p>
    </div>
    <div class="total-item total-balance">
        <h3><i class="fas fa-balance-scale"></i> Баланс</h3>
        <p class="balance-<%= totalBalance.compareTo(BigDecimal.ZERO) > 0 ? "positive" : totalBalance.compareTo(BigDecimal.ZERO) < 0 ? "negative" : "zero" %>">
            <%= totalBalance %> ₽
        </p>
    </div>
</div>