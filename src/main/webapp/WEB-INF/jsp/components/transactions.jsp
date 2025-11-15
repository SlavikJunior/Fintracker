<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.slavikjunior.models.TransactionGroup" %>
<%@ page import="com.slavikjunior.models.TransactionItem" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.math.BigDecimal" %>
<%
  List<TransactionGroup> transactionGroups = (List<TransactionGroup>) request.getAttribute("transactionGroups");
  SimpleDateFormat dateFormat = (SimpleDateFormat) request.getAttribute("dateFormat");
  SimpleDateFormat dayFormat = (SimpleDateFormat) request.getAttribute("dayFormat");
  SimpleDateFormat monthFormat = (SimpleDateFormat) request.getAttribute("monthFormat");
%>

<h2><i class="fas fa-history"></i> История транзакций</h2>
<% if (transactionGroups != null && !transactionGroups.isEmpty()) {
  for (TransactionGroup group : transactionGroups) { %>
<div class="day-header">
  <div class="day-info">
    <h3><%= dayFormat.format(group.getDate()) %></h3>
    <span><%= monthFormat.format(group.getDate()) %></span>
  </div>
  <div class="day-totals">
    <span class="income-amount">+<%= group.getDayIncome() %> ₽</span>
    <span class="expense-amount">-<%= group.getDayExpense() %> ₽</span>
    <span class="<%=
                group.getDayBalance().compareTo(BigDecimal.ZERO) > 0 ? "balance-positive" :
                group.getDayBalance().compareTo(BigDecimal.ZERO) < 0 ? "balance-negative" : "balance-zero" %>">
                <%= group.getDayBalance().compareTo(BigDecimal.ZERO) > 0 ? "+" : "" %><%= group.getDayBalance() %> ₽
            </span>
  </div>
</div>

<table class="transaction-table">
  <thead>
  <tr>
    <th>Тип</th>
    <th>Сумма</th>
    <th>Категория</th>
    <th>Описание</th>
    <th>Теги</th>
    <th>Время</th>
    <th>Действия</th>
  </tr>
  </thead>
  <tbody>
  <% for (TransactionItem transaction : group.getTransactions()) { %>
  <tr>
    <td class="type">
      <% if ("INCOME".equals(transaction.getType())) { %>
      <span class="income-badge"><i class="fas fa-arrow-up"></i> Доход</span>
      <% } else { %>
      <span class="expense-badge"><i class="fas fa-arrow-down"></i> Расход</span>
      <% } %>
    </td>
    <td class="<%= "INCOME".equals(transaction.getType()) ? "income-amount" : "expense-amount" %>">
      <%= "INCOME".equals(transaction.getType()) ? "+" : "-" %><%= transaction.getAmount() %> ₽
    </td>
    <td class="category"><%= transaction.getCategory() %></td>
    <td class="description"><%= transaction.getDescription() != null ? transaction.getDescription() : "" %></td>
    <td class="tags">
      <% for (String tagName : transaction.getTagNames()) { %>
      <span class="tag-badge">
                    <%= tagName %>
                </span>
      <% } %>
      <% if (transaction.getTagNames().isEmpty()) { %>
      <span class="no-tags">—</span>
      <% } %>
    </td>
    <td class="date">
      <%= dateFormat.format(transaction.getCreatedAt()) %>
    </td>
    <td class="actions">
      <!-- Кнопка управления тегами -->
      <button type="button" class="btn btn-secondary btn-small manage-tags-btn"
              data-transaction-id="<%= transaction.getId() %>"
              data-transaction-type="<%= transaction.getType() %>"
              title="Управление тегами">
        <i class="fas fa-tags"></i>
      </button>

      <!-- Форма удаления транзакции -->
      <form action="${pageContext.request.contextPath}/transactions/delete" method="post"
            onsubmit="return confirm('Удалить эту транзакцию?')" style="display: inline;">
        <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>">
        <input type="hidden" name="transactionId" value="<%= transaction.getId() %>">
        <input type="hidden" name="transactionType" value="<%= transaction.getType() %>">
        <button type="submit" class="btn btn-danger btn-small" title="Удалить транзакцию">
          <i class="fas fa-trash"></i>
        </button>
      </form>
    </td>
  </tr>
  <% } %>
  </tbody>
</table>
<% }
} else { %>
<p class="no-data"><i class="fas fa-info-circle"></i> У вас пока нет транзакций</p>
<% } %>