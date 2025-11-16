<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="userTags" value="${requestScope.userTags}" />
<c:if test="${empty userTags}">
    <c:set var="userTags" value="<%= java.util.Collections.emptyList() %>" />
</c:if>

<div id="tagModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h3><i class="fas fa-tags"></i> Управление тегами транзакции</h3>

        <form id="tagTransactionForm" method="post" action="${pageContext.request.contextPath}/tags">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <input type="hidden" id="modalTransactionId" name="transactionId">

            <div class="tag-selection">
                <h4>Выберите теги:</h4>
                <div class="tag-checkboxes">
                    <c:choose>
                        <c:when test="${not empty userTags}">
                            <c:forEach items="${userTags}" var="tag">
                                <label class="tag-checkbox">
                                    <input type="checkbox" name="tagIds" value="${tag.id}">
                                    <span class="tag-badge" style="background-color: ${tag.color != null ? tag.color : '#6498d4'};">
                                            ${tag.name}
                                    </span>
                                </label>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p class="no-tags">У вас пока нет тегов. Создайте теги в разделе "Управление тегами".</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="modal-actions">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Сохранить теги
                </button>
                <button type="button" class="btn btn-secondary" id="closeModal">
                    <i class="fas fa-times"></i> Отмена
                </button>
            </div>
        </form>
    </div>
</div>