<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <span><i class="fas fa-user-circle"></i> Пользователь: ${sessionScope.user_login}</span>
        <a href="${pageContext.request.contextPath}/auth/logout" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Выйти</a>
    </div>
</header>

<c:if test="${not empty param.error}">
    <div class="error-message">
        <i class="fas fa-exclamation-triangle"></i>
        <c:choose>
            <c:when test="${param.error == 'invalid_data'}">Неверные данные транзакции</c:when>
            <c:when test="${param.error == 'invalid_amount'}">Неверная сумма</c:when>
            <c:when test="${param.error == 'db_error'}">Ошибка базы данных</c:when>
            <c:when test="${param.error == 'invalid_id'}">Неверный ID транзакции</c:when>
            <c:when test="${param.error == 'delete_failed'}">Ошибка при удалении</c:when>
            <c:otherwise>Произошла ошибка</c:otherwise>
        </c:choose>
    </div>
</c:if>

<c:if test="${not empty param.success}">
    <div class="success-message">
        <i class="fas fa-check-circle"></i>
        <c:choose>
            <c:when test="${param.success == 'true'}">Транзакция успешно добавлена</c:when>
            <c:when test="${param.success == 'deleted'}">Транзакция удалена</c:when>
        </c:choose>
    </div>
</c:if>

<div class="container">
    <section class="add-transaction">
        <h2><i class="fas fa-plus-circle"></i> Добавить транзакцию</h2>
        <form action="${pageContext.request.contextPath}/transactions" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

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
        <c:choose>
            <c:when test="${not empty transactions}">
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
                    <c:forEach var="transaction" items="${transactions}">
                        <tr>
                            <td class="amount">${transaction.amount} ₽</td>
                            <td class="category">${transaction.category}</td>
                            <td class="description">${transaction.description}</td>
                            <td class="date">
                                <fmt:formatDate value="${transaction.createdAt}" pattern="dd.MM.yyyy HH:mm"/>
                            </td>
                            <td class="actions">
                                <form action="${pageContext.request.contextPath}/transactions/delete" method="post"
                                      onsubmit="return confirm('Удалить эту транзакцию?')">
                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                    <input type="hidden" name="transactionId" value="${transaction.id}">
                                    <button type="submit" class="btn btn-danger btn-small"><i class="fas fa-trash"></i> Удалить</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="no-data"><i class="fas fa-info-circle"></i> У вас пока нет транзакций</p>
            </c:otherwise>
        </c:choose>
    </section>
</div>
</body>
</html>