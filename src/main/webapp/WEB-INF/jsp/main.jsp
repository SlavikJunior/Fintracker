<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.slavikjunior.models.Transaction" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.slavikjunior.util.SessionConstants" %>
<%
    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
    String error = request.getParameter("error");
    String success = request.getParameter("success");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
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
                <label for="amount"><i class="fas fa-ruble-sign"></i> Сумма:</label>
                <input type="number" step="0.01" id="amount" name="amount" placeholder="0.00" required>
            </div>

            <div class="form-group">
                <label for="category"><i class="fas fa-tags"></i> Категория:</label>
                <select id="category" name="category" required>
                    <option value="">Выберите категорию</option>
                    <option value="Еда">Еда</option>
                    <option value="Транспорт">Транспорт</option>
                    <option value="Жилье">Жилье</option>
                    <option value="Развлечения">Развлечения</option>
                    <option value="Здоровье">Здоровье</option>
                    <option value="Одежда">Одежда</option>
                    <option value="Образование">Образование</option>
                    <option value="Другое">Другое</option>
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
        <h2><i class="fas fa-history"></i> История транзакций</h2>
        <% if (transactions != null && !transactions.isEmpty()) { %>
        <table class="transaction-table">
            <thead>
            <tr>
                <th>Сумма</th>
                <th>Категория</th>
                <th>Описание</th>
                <th>Дата</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <% for (Transaction transaction : transactions) { %>
            <tr>
                <td class="amount"><%= transaction.getAmount() %> ₽</td>
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
        <% } else { %>
        <p class="no-data"><i class="fas fa-info-circle"></i> У вас пока нет транзакций</p>
        <% } %>
    </section>
</div>
</body>
</html>