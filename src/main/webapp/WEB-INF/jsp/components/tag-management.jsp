<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="userTags" value="${requestScope.userTags}" />
<c:if test="${empty userTags}">
    <c:set var="userTags" value="<%= java.util.Collections.emptyList() %>" />
</c:if>

<div class="tag-management">
    <h3><i class="fas fa-tags"></i> Управление тегами</h3>

    <form action="${pageContext.request.contextPath}/main" method="post" class="tag-form">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        <input type="hidden" name="action" value="createTag">

        <div class="form-row">
            <div class="form-group">
                <label for="tagName">Название тега:</label>
                <input type="text" id="tagName" name="tagName" placeholder="Например: работа" required maxlength="20">
            </div>

            <div class="form-group">
                <label for="tagColor">Цвет:</label>
                <input type="color" id="tagColor" name="tagColor" value="#6498d4" title="Выберите цвет тега">
            </div>

            <div class="form-group">
                <button type="submit" class="btn btn-secondary btn-small">
                    <i class="fas fa-plus"></i> Создать тег
                </button>
            </div>
        </div>
    </form>

    <div class="tag-list">
        <c:choose>
            <c:when test="${not empty userTags}">
                <c:forEach items="${userTags}" var="tag">
                    <div class="tag-item">
                        <span class="tag-badge" style="background-color: ${tag.color != null ? tag.color : '#6498d4'};">
                                ${tag.name}
                        </span>
                        <form action="${pageContext.request.contextPath}/main" method="post"
                              onsubmit="return confirm('Удалить тег &quot;${tag.name}&quot;? Все связи с транзакциями будут удалены.')">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="action" value="deleteTag">
                            <input type="hidden" name="tagId" value="${tag.id}">
                            <button type="submit" class="btn btn-danger btn-small" title="Удалить тег">
                                <i class="fas fa-times"></i>
                            </button>
                        </form>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p class="no-tags">У вас пока нет тегов. Создайте первый тег!</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>