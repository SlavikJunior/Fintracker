<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.math.BigDecimal" %>
<%
    String totalIncomeStr = request.getParameter("totalIncome");
    String totalExpenseStr = request.getParameter("totalExpense");
    String totalBalanceStr = request.getParameter("totalBalance");

    BigDecimal totalIncome = new BigDecimal(totalIncomeStr);
    BigDecimal totalExpense = new BigDecimal(totalExpenseStr);
    BigDecimal totalBalance = new BigDecimal(totalBalanceStr);
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
        <p class="<%=
            totalBalance.compareTo(BigDecimal.ZERO) > 0 ? "balance-positive" :
            totalBalance.compareTo(BigDecimal.ZERO) < 0 ? "balance-negative" : "balance-zero"
        %>">
            <%= totalBalance %> ₽
        </p>
    </div>
</div>