<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%
    List<String> incomeCategories = (List<String>) request.getAttribute("incomeCategories");
    List<String> expenseCategories = (List<String>) request.getAttribute("expenseCategories");
    if (incomeCategories == null) incomeCategories = java.util.Collections.emptyList();
    if (expenseCategories == null) expenseCategories = java.util.Collections.emptyList();
%>

<h2><i class="fas fa-plus-circle"></i> Добавить транзакцию</h2>
<form action="${pageContext.request.contextPath}/transactions" method="post">
    <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>">

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
                <% for (String category : incomeCategories) { %>
                <option value="<%= category %>"><%= category %></option>
                <% } %>
            </optgroup>
            <optgroup label="Расходы" id="expense-categories">
                <% for (String category : expenseCategories) { %>
                <option value="<%= category %>"><%= category %></option>
                <% } %>
            </optgroup>
        </select>
    </div>

    <div class="form-group">
        <label for="description"><i class="fas fa-pencil-alt"></i> Описание:</label>
        <input type="text" id="description" name="description" placeholder="Краткое описание">
    </div>

    <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Добавить</button>
</form>