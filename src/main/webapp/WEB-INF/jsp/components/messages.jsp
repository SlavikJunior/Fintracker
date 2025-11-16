<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="error" value="${param.error}" />
<c:set var="success" value="${param.success}" />

<c:if test="${not empty error}">
    <div class="error-message">
        <i class="fas fa-exclamation-triangle"></i>
        <c:choose>
            <c:when test="${error == 'invalid_data'}">Неверные данные транзакции</c:when>
            <c:when test="${error == 'invalid_amount'}">Неверная сумма</c:when>
            <c:when test="${error == 'db_error'}">Ошибка базы данных</c:when>
            <c:when test="${error == 'invalid_id'}">Неверный ID транзакции</c:when>
            <c:when test="${error == 'delete_failed'}">Ошибка при удалении</c:when>
            <c:when test="${error == 'invalid_tag_name'}">Неверное название тега</c:when>
            <c:when test="${error == 'invalid_tag_id'}">Неверный ID тега</c:when>
            <c:when test="${error == 'tag_creation_failed'}">Ошибка при создании тега</c:when>
            <c:when test="${error == 'tag_deletion_failed'}">Ошибка при удалении тега</c:when>
            <c:when test="${error == 'tag_already_exists'}">Тег с таким названием уже существует</c:when>
            <c:when test="${error == 'tag_update_failed'}">Ошибка при обновлении тегов</c:when>
            <c:otherwise>Произошла ошибка</c:otherwise>
        </c:choose>
    </div>
</c:if>

<c:if test="${not empty success}">
    <div class="success-message">
        <i class="fas fa-check-circle"></i>
        <c:choose>
            <c:when test="${success == 'true'}">Транзакция успешно добавлена</c:when>
            <c:when test="${success == 'deleted'}">Транзакция удалена</c:when>
            <c:when test="${success == 'tag_created'}">Тег успешно создан</c:when>
            <c:when test="${success == 'tag_deleted'}">Тег успешно удален</c:when>
            <c:when test="${success == 'tags_updated'}">Теги транзакции обновлены</c:when>
        </c:choose>
    </div>
</c:if>