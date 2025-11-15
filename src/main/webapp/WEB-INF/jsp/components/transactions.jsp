<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List,com.slavikjunior.models.*,java.text.SimpleDateFormat,java.math.BigDecimal" %>
<%
  List<TransactionGroup> transactionGroups = (List<TransactionGroup>) request.getAttribute("transactionGroups");
  SimpleDateFormat dateFormat = (SimpleDateFormat) request.getAttribute("dateFormat");
  SimpleDateFormat dayFormat = (SimpleDateFormat) request.getAttribute("dayFormat");
  SimpleDateFormat monthFormat = (SimpleDateFormat) request.getAttribute("monthFormat");

  if (dateFormat == null) dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
  if (dayFormat == null) dayFormat = new SimpleDateFormat("dd.MM.yyyy");
  if (monthFormat == null) monthFormat = new SimpleDateFormat("MMMM yyyy", new java.util.Locale("ru"));
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
    <span class="balance-<%= group.getDayBalance().compareTo(BigDecimal.ZERO) > 0 ? "positive" :
                          group.getDayBalance().compareTo(BigDecimal.ZERO) < 0 ? "negative" : "zero" %>">
      <%= group.getDayBalance().compareTo(BigDecimal.ZERO) > 0 ? "+" : "" %><%= group.getDayBalance() %> ₽
    </span>
  </div>
</div>

<table class="transaction-table">
  <thead><tr><th>Тип</th><th>Сумма</th><th>Категория</th><th>Описание</th><th>Теги</th><th>Время</th><th>Действия</th></tr></thead>
  <tbody>
  <% for (TransactionItem t : group.getTransactions()) { %>
  <tr>
    <td class="type">
      <span class="<%= "INCOME".equals(t.getType()) ? "income" : "expense" %>-badge">
        <i class="fas fa-arrow-<%= "INCOME".equals(t.getType()) ? "up" : "down" %>"></i>
        <%= "INCOME".equals(t.getType()) ? "Доход" : "Расход" %>
      </span>
    </td>
    <td class="<%= "INCOME".equals(t.getType()) ? "income" : "expense" %>-amount">
      <%= "INCOME".equals(t.getType()) ? "+" : "-" %><%= t.getAmount() %> ₽
    </td>
    <td class="category"><%= t.getCategory() %></td>
    <td class="description"><%= t.getDescription() != null ? t.getDescription() : "" %></td>
    <td class="tags">
      <% if (!t.getTagNames().isEmpty()) {
        for (String tagName : t.getTagNames()) { %>
      <span class="tag-badge"><%= tagName %></span>
      <% } } else { %>
      <span class="no-tags">—</span>
      <% } %>
    </td>
    <td class="date"><%= dateFormat.format(t.getCreatedAt()) %></td>
    <td class="actions">
      <button type="button" class="btn btn-small manage-tags-btn"
              data-transaction-id="<%= t.getId() %>" data-transaction-type="<%= t.getType() %>"
              title="Управление тегами"><i class="fas fa-tags"></i></button>
      <form action="${pageContext.request.contextPath}/transactions/delete" method="post"
            onsubmit="return confirm('Удалить эту транзакцию?')" style="display: inline;">
        <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>">
        <input type="hidden" name="transactionId" value="<%= t.getId() %>">
        <input type="hidden" name="transactionType" value="<%= t.getType() %>">
        <button type="submit" class="btn btn-danger btn-small" title="Удалить"><i class="fas fa-trash"></i></button>
      </form>
    </td>
  </tr>
  <% } %>
  </tbody>
</table>
<% } } else { %>
<p class="no-data"><i class="fas fa-info-circle"></i> У вас пока нет транзакций</p>
<% } %>