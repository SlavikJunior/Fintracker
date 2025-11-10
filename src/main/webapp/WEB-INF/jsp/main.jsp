<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.slavikjunior.models.Transaction" %>
<%@ page import="com.slavikjunior.models.TransactionGroup" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.slavikjunior.util.SessionConstants" %>
<%@ page import="java.math.BigDecimal" %>
<%
    List<TransactionGroup> transactionGroups = (List<TransactionGroup>) request.getAttribute("transactionGroups");
    BigDecimal totalIncome = (BigDecimal) request.getAttribute("totalIncome");
    BigDecimal totalExpense = (BigDecimal) request.getAttribute("totalExpense");
    BigDecimal totalBalance = (BigDecimal) request.getAttribute("totalBalance");

    String error = request.getParameter("error");
    String success = request.getParameter("success");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", new java.util.Locale("ru"));
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI FinTracker - Главная</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<header class="header">
    <h1><i class="fas fa-wallet"></i> AI FinTracker</h1>
    <p>Добро пожаловать! Ваш помощник по учету расходов</p>
    <div class="user-info">
        <span><i class="fas fa-user-circle"></i> Пользователь: <%= session.getAttribute(SessionConstants.USER_LOGIN) %></span>
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Выйти</a>
    </div>
</header>

<% if (error != null) { %>
<div class="error-message">
    <i class="fas fa-exclamation-triangle"></i>
    <%
        String errorMessage = "";
        switch(error) {
            case "invalid_data": errorMessage = "Неверные данные транзакции"; break;
            case "invalid_amount": errorMessage = "Неверная сумма"; break;
            case "db_error": errorMessage = "Ошибка базы данных"; break;
            case "invalid_id": errorMessage = "Неверный ID транзакции"; break;
            case "delete_failed": errorMessage = "Ошибка при удалении"; break;
            default: errorMessage = "Произошла ошибка";
        }
    %>
    <%= errorMessage %>
</div>
<% } %>

<% if (success != null) { %>
<div class="success-message">
    <i class="fas fa-check-circle"></i>
    <%
        String successMessage = "";
        if ("true".equals(success)) {
            successMessage = "Транзакция успешно добавлена";
        } else if ("deleted".equals(success)) {
            successMessage = "Транзакция удалена";
        }
    %>
    <%= successMessage %>
</div>
<% } %>

<div class="container">
    <section class="add-transaction">
        <h2><i class="fas fa-plus-circle"></i> Добавить транзакцию</h2>
        <form action="${pageContext.request.contextPath}/transactions" method="post">
            <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>">

            <div class="form-group">
                <label for="type"><i class="fas fa-exchange-alt"></i> Тип операции:</label>
                <select id="type" name="type" required>
                    <option value="EXPENSE">Расход</option>
                    <option value="INCOME">Доход</option>
                </select>
            </div>

            <div class="form-group">
                <label for="amount"><i class="fas fa-ruble-sign"></i> Сумма:</label>
                <input type="number" step="0.01" id="amount" name="amount" placeholder="0.00" required>
            </div>

            <div class="form-group">
                <label for="category"><i class="fas fa-tags"></i> Категория:</label>
                <select id="category" name="category" required>
                    <option value="">Выберите категорию</option>
                    <optgroup label="Доходы">
                        <option value="Зарплата">Зарплата</option>
                        <option value="Фриланс">Фриланс</option>
                        <option value="Инвестиции">Инвестиции</option>
                        <option value="Подарки">Подарки</option>
                        <option value="Прочее">Прочее</option>
                    </optgroup>
                    <optgroup label="Расходы">
                        <option value="Еда">Еда</option>
                        <option value="Транспорт">Транспорт</option>
                        <option value="Жилье">Жилье</option>
                        <option value="Развлечения">Развлечения</option>
                        <option value="Здоровье">Здоровье</option>
                        <option value="Одежда">Одежда</option>
                        <option value="Образование">Образование</option>
                        <option value="Другое">Другое</option>
                    </optgroup>
                </select>
            </div>

            <div class="form-group">
                <label for="description"><i class="fas fa-pencil-alt"></i> Описание:</label>
                <input type="text" id="description" name="description" placeholder="Краткое описание">
            </div>

            <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Добавить</button>
        </form>
    </section>

    <section class="transactions">
        <div class="total-summary">
            <div class="total-item total-income">
                <h3><i class="fas fa-arrow-up"></i> Общие доходы</h3>
                <p class="income-amount"><%= totalIncome != null ? totalIncome : "0.00" %> ₽</p>
            </div>
            <div class="total-item total-expense">
                <h3><i class="fas fa-arrow-down"></i> Общие расходы</h3>
                <p class="expense-amount"><%= totalExpense != null ? totalExpense : "0.00" %> ₽</p>
            </div>
            <div class="total-item total-balance">
                <h3><i class="fas fa-balance-scale"></i> Баланс</h3>
                <p class="<%=
                    totalBalance != null ?
                    (totalBalance.compareTo(BigDecimal.ZERO) > 0 ? "balance-positive" :
                     totalBalance.compareTo(BigDecimal.ZERO) < 0 ? "balance-negative" : "balance-zero")
                    : "balance-zero" %>">
                    <%= totalBalance != null ? totalBalance : "0.00" %> ₽
                </p>
            </div>
        </div>

        <div class="filters-section">
            <h3><i class="fas fa-filter"></i> Фильтры</h3>
            <form method="get" action="${pageContext.request.contextPath}/main">
                <div class="filter-row">
                    <div class="filter-group">
                        <label for="typeFilter">Тип:</label>
                        <select id="typeFilter" name="type">
                            <option value="">Все</option>
                            <option value="INCOME">Доходы</option>
                            <option value="EXPENSE">Расходы</option>
                        </select>
                    </div>
                    <div class="filter-group">
                        <label for="categoryFilter">Категория:</label>
                        <select id="categoryFilter" name="category">
                            <option value="">Все категории</option>
                        </select>
                    </div>
                    <div class="filter-group">
                        <label for="startDate">С:</label>
                        <input type="date" id="startDate" name="startDate">
                    </div>
                    <div class="filter-group">
                        <label for="endDate">По:</label>
                        <input type="date" id="endDate" name="endDate">
                    </div>
                    <div class="filter-actions">
                        <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Применить</button>
                        <a href="${pageContext.request.contextPath}/main" class="btn btn-secondary"><i class="fas fa-times"></i> Сбросить</a>
                    </div>
                </div>
            </form>
        </div>

        <h2><i class="fas fa-history"></i> История транзакций</h2>
        <% if (transactionGroups != null && !transactionGroups.isEmpty()) {
            for (TransactionGroup group : transactionGroups) {
        %>
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
                <th>Время</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <% for (Transaction transaction : group.getTransactions()) { %>
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
                <td class="date">
                    <%= dateFormat.format(transaction.getCreatedAt()) %>
                </td>
                <td class="actions">
                    <form action="${pageContext.request.contextPath}/transactions/delete" method="post"
                          onsubmit="return confirm('Удалить эту транзакцию?')">
                        <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>">
                        <input type="hidden" name="transactionId" value="<%= transaction.getId() %>">
                        <button type="submit" class="btn btn-danger btn-small"><i class="fas fa-trash"></i> Удалить</button>
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
    </section>
</div>

<footer class="footer">
    <div class="footer-content">
        <span><i class="fas fa-wallet"></i> AI FinTracker</span>
        <span>•</span>
        <span>Управляйте финансами легко</span>
        <span>•</span>
        <span>© 2025</span>
    </div>
</footer>
</body>
</html>