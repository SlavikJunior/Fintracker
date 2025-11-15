<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List,com.slavikjunior.models.*,java.text.SimpleDateFormat,java.math.BigDecimal,java.util.*" %>
<%
    // Данные из request с проверками
    List<TransactionGroup> transactionGroups = (List<TransactionGroup>) request.getAttribute("transactionGroups");
    if (transactionGroups == null) transactionGroups = Collections.emptyList();

    BigDecimal totalIncome = (BigDecimal) request.getAttribute("totalIncome");
    BigDecimal totalExpense = (BigDecimal) request.getAttribute("totalExpense");
    BigDecimal totalBalance = (BigDecimal) request.getAttribute("totalBalance");

    List<String> incomeCategories = (List<String>) request.getAttribute("incomeCategories");
    List<String> expenseCategories = (List<String>) request.getAttribute("expenseCategories");
    List<String> allCategories = (List<String>) request.getAttribute("allCategories");
    List<Tag> userTags = (List<Tag>) request.getAttribute("userTags");

    // Форматы дат
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", new Locale("ru"));

    // Устанавливаем в request для компонентов
    request.setAttribute("dateFormat", dateFormat);
    request.setAttribute("dayFormat", dayFormat);
    request.setAttribute("monthFormat", monthFormat);
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
<jsp:include page="components/header.jsp"/>

<jsp:include page="components/messages.jsp"/>

<div class="container">
    <section class="add-transaction">
        <jsp:include page="components/add-transaction.jsp"/>
        <jsp:include page="components/tag-management.jsp"/>
    </section>

    <section class="transactions">
        <jsp:include page="components/totals.jsp"/>
        <jsp:include page="components/filters.jsp"/>
        <jsp:include page="components/transactions.jsp"/>
    </section>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
<jsp:include page="components/tag-modal.jsp"/>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>