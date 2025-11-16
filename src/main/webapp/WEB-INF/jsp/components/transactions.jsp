<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h2><i class="fas fa-history"></i> История транзакций</h2>
<c:choose>
  <c:when test="${not empty transactionGroups}">
    <c:forEach items="${transactionGroups}" var="group">
      <div class="day-header">
        <div class="day-info">
          <!-- ПРЕОБРАЗУЕМ java.sql.Date → String → Date -->
          <fmt:formatDate value="${group.date}" pattern="dd.MM.yyyy" var="formattedDay" />
          <h3>${formattedDay}</h3>
        </div>
        <div class="day-totals">
          <span class="income-amount">+${group.dayIncome} ₽</span>
          <span class="expense-amount">-${group.dayExpense} ₽</span>
          <span class="balance-${group.dayBalance > 0 ? 'positive' : group.dayBalance < 0 ? 'negative' : 'zero'}">
            ${group.dayBalance > 0 ? '+' : ''}${group.dayBalance} ₽
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
        <c:forEach items="${group.transactions}" var="transaction">
          <tr>
            <td class="type">
              <c:choose>
                <c:when test="${transaction.type == 'INCOME'}">
                  <span class="income-badge">Доход</span>
                </c:when>
                <c:otherwise>
                  <span class="expense-badge">Расход</span>
                </c:otherwise>
              </c:choose>
            </td>
            <td class="${transaction.type == 'INCOME' ? 'income-amount' : 'expense-amount'}">
                ${transaction.type == 'INCOME' ? '+' : '-'}${transaction.amount} ₽
            </td>
            <td class="category">${transaction.category}</td>
            <td class="description">${transaction.description != null ? transaction.description : ''}</td>
            <td class="tags">
              <c:choose>
                <c:when test="${not empty transaction.tags}">
                  <c:forEach items="${transaction.tags}" var="tag">
                      <span class="tag-badge" style="background-color: ${tag.color != null ? tag.color : '#6498d4'};">
                          ${tag.name}
                      </span>
                  </c:forEach>
                </c:when>
                <c:otherwise>
                  <span class="no-tags">—</span>
                </c:otherwise>
              </c:choose>
            </td>
            <td class="date">
              <fmt:formatDate value="${transaction.createdAt}" pattern="dd.MM.yyyy HH:mm" />
            </td>
            <td class="actions">
              <button type="button" class="btn btn-secondary btn-small manage-tags-btn"
                      data-transaction-id="${transaction.id}"
                      title="Управление тегами">
                Теги
              </button>

              <form action="${pageContext.request.contextPath}/transactions/delete" method="post"
                    onsubmit="return confirm('Удалить эту транзакцию?')">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                <input type="hidden" name="transactionId" value="${transaction.id}">
                <button type="submit" class="btn btn-danger btn-small" title="Удалить транзакцию">
                  Удалить
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:forEach>
  </c:when>
  <c:otherwise>
    <p class="no-data">У вас пока нет транзакций</p>
  </c:otherwise>
</c:choose>