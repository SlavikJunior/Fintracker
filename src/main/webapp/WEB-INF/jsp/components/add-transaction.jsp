<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="incomeCategories" value="${requestScope.incomeCategories}" />
<c:if test="${empty incomeCategories}">
    <c:set var="incomeCategories" value="<%= java.util.Collections.emptyList() %>" />
</c:if>
<c:set var="expenseCategories" value="${requestScope.expenseCategories}" />
<c:if test="${empty expenseCategories}">
    <c:set var="expenseCategories" value="<%= java.util.Collections.emptyList() %>" />
</c:if>

<h2><i class="fas fa-plus-circle"></i> Добавить транзакцию</h2>
<form action="${pageContext.request.contextPath}/transactions" method="post">
    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

    <div class="form-group">
        <label for="type"><i class="fas fa-exchange-alt"></i> Тип операции:</label>
        <select id="type" name="type" required onchange="updateCategories()">
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
            <optgroup label="Доходы" id="income-categories">
                <c:forEach items="${incomeCategories}" var="category">
                    <option value="${category}">${category}</option>
                </c:forEach>
            </optgroup>
            <optgroup label="Расходы" id="expense-categories">
                <c:forEach items="${expenseCategories}" var="category">
                    <option value="${category}">${category}</option>
                </c:forEach>
            </optgroup>
        </select>
    </div>

    <div class="form-group">
        <label for="description"><i class="fas fa-pencil-alt"></i> Описание:</label>
        <input type="text" id="description" name="description" placeholder="Краткое описание">
    </div>

    <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Добавить</button>
</form>