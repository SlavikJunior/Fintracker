<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.slavikjunior.models.Tag" %>
<%@ page import="java.util.List" %>
<%
    List<Tag> userTags = (List<Tag>) request.getAttribute("userTags");
    if (userTags == null) userTags = java.util.Collections.emptyList();
%>

<div class="tag-management">
    <h3><i class="fas fa-tags"></i> Управление тегами</h3>

    <form action="${pageContext.request.contextPath}/main" method="post" class="tag-form">
        <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>">
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
        <% if (!userTags.isEmpty()) { %>
        <% for (Tag tag : userTags) { %>
        <div class="tag-item">
                <span class="tag-badge" style="background-color: <%= tag.getColor() != null ? tag.getColor() : "#6498d4" %>;">
                    <%= tag.getName() %>
                </span>
            <form action="${pageContext.request.contextPath}/main" method="post"
                  onsubmit="return confirm('Удалить тег &quot;<%= tag.getName() %>&quot;? Все связи с транзакциями будут удалены.')">
                <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>">
                <input type="hidden" name="action" value="deleteTag">
                <input type="hidden" name="tagId" value="<%= tag.getId() %>">
                <button type="submit" class="btn btn-danger btn-small" title="Удалить тег">
                    <i class="fas fa-times"></i>
                </button>
            </form>
        </div>
        <% } %>
        <% } else { %>
        <p class="no-tags">У вас пока нет тегов. Создайте первый тег!</p>
        <% } %>
    </div>
</div>