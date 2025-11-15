<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = request.getParameter("error");
    String success = request.getParameter("success");
%>

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
            case "invalid_tag_name": errorMessage = "Неверное название тега"; break;
            case "invalid_tag_id": errorMessage = "Неверный ID тега"; break;
            case "tag_creation_failed": errorMessage = "Ошибка при создании тега"; break;
            case "tag_deletion_failed": errorMessage = "Ошибка при удалении тега"; break;
            case "tag_already_exists": errorMessage = "Тег с таким названием уже существует"; break;
            case "tag_update_failed": errorMessage = "Ошибка при обновлении тегов"; break;
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
        } else if ("tag_created".equals(success)) {
            successMessage = "Тег успешно создан";
        } else if ("tag_deleted".equals(success)) {
            successMessage = "Тег успешно удален";
        } else if ("tags_updated".equals(success)) {
            successMessage = "Теги транзакции обновлены";
        }
    %>
    <%= successMessage %>
</div>
<% } %>