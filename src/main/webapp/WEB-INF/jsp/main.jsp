<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.slavikjunior.models.TransactionGroup" %>
<%@ page import="com.slavikjunior.models.TransactionItem" %>
<%@ page import="com.slavikjunior.models.Tag" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.slavikjunior.util.SessionConstants" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Arrays" %>
<%
    // Данные из request
    List<TransactionGroup> transactionGroups = (List<TransactionGroup>) request.getAttribute("transactionGroups");
    if (transactionGroups == null) transactionGroups = Collections.emptyList();

    BigDecimal totalIncome = (BigDecimal) request.getAttribute("totalIncome");
    BigDecimal totalExpense = (BigDecimal) request.getAttribute("totalExpense");
    BigDecimal totalBalance = (BigDecimal) request.getAttribute("totalBalance");

    List<String> incomeCategories = (List<String>) request.getAttribute("incomeCategories");
    if (incomeCategories == null) incomeCategories = Collections.emptyList();

    List<String> expenseCategories = (List<String>) request.getAttribute("expenseCategories");
    if (expenseCategories == null) expenseCategories = Collections.emptyList();

    List<String> allCategories = (List<String>) request.getAttribute("allCategories");
    if (allCategories == null) allCategories = Collections.emptyList();

    List<Tag> userTags = (List<Tag>) request.getAttribute("userTags");
    if (userTags == null) userTags = Collections.emptyList();

    List<String> userTagNames = (List<String>) request.getAttribute("userTagNames");
    if (userTagNames == null) userTagNames = Collections.emptyList();

    // Параметры
    String error = request.getParameter("error");
    String success = request.getParameter("success");
    String filterType = (String) request.getAttribute("filterType");
    String filterCategory = (String) request.getAttribute("filterCategory");
    String filterTag = (String) request.getAttribute("filterTag");
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");

    // Форматы дат
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", new java.util.Locale("ru"));

    // Создаем строки для передачи в компоненты
    String incomeCategoriesStr = String.join(",", incomeCategories);
    String expenseCategoriesStr = String.join(",", expenseCategories);
    String allCategoriesStr = String.join(",", allCategories);
    String userTagNamesStr = String.join(",", userTagNames);
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FinTracker - Главная</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<!-- Шапка -->
<jsp:include page="components/header.jsp"/>

<!-- Сообщения -->
<jsp:include page="components/messages.jsp">
    <jsp:param name="error" value="<%= error != null ? error : \"\" %>"/>
    <jsp:param name="success" value="<%= success != null ? success : \"\" %>"/>
</jsp:include>

<div class="container">
    <!-- Левая колонка -->
    <section class="add-transaction">
        <jsp:include page="components/add-transaction.jsp">
            <jsp:param name="incomeCategories" value="<%= incomeCategoriesStr %>"/>
            <jsp:param name="expenseCategories" value="<%= expenseCategoriesStr %>"/>
        </jsp:include>

        <jsp:include page="components/tag-management.jsp">
            <jsp:param name="userTags" value="<%= userTags %>"/>
        </jsp:include>
    </section>

    <!-- Правая колонка -->
    <section class="transactions">
        <jsp:include page="components/totals.jsp">
            <jsp:param name="totalIncome" value="<%= totalIncome != null ? totalIncome.toString() : \"0.00\" %>"/>
            <jsp:param name="totalExpense" value="<%= totalExpense != null ? totalExpense.toString() : \"0.00\" %>"/>
            <jsp:param name="totalBalance" value="<%= totalBalance != null ? totalBalance.toString() : \"0.00\" %>"/>
        </jsp:include>

        <jsp:include page="components/filters.jsp">
            <jsp:param name="filterType" value="<%= filterType != null ? filterType : \"\" %>"/>
            <jsp:param name="filterCategory" value="<%= filterCategory != null ? filterCategory : \"\" %>"/>
            <jsp:param name="filterTag" value="<%= filterTag != null ? filterTag : \"\" %>"/>
            <jsp:param name="startDate" value="<%= startDate != null ? startDate : \"\" %>"/>
            <jsp:param name="endDate" value="<%= endDate != null ? endDate : \"\" %>"/>
            <jsp:param name="allCategories" value="<%= allCategoriesStr %>"/>
            <jsp:param name="userTagNames" value="<%= userTagNamesStr %>"/>
        </jsp:include>

        <jsp:include page="components/transactions.jsp">
            <jsp:param name="transactionGroups" value="<%= transactionGroups %>"/>
            <jsp:param name="dateFormat" value="<%= dateFormat %>"/>
            <jsp:param name="dayFormat" value="<%= dayFormat %>"/>
            <jsp:param name="monthFormat" value="<%= monthFormat %>"/>
        </jsp:include>
    </section>
</div>

<!-- Футер -->
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>

<!-- Модальное окно тегов -->
<jsp:include page="components/tag-modal.jsp">
    <jsp:param name="userTags" value="<%= userTags %>"/>
</jsp:include>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>