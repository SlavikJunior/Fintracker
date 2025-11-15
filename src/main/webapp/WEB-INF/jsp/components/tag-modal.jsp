<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.slavikjunior.models.Tag" %>
<%@ page import="java.util.List" %>
<%
    List<Tag> userTags = (List<Tag>) request.getAttribute("userTags");
    if (userTags == null) userTags = java.util.Collections.emptyList();
%>

<div id="tagModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h3><i class="fas fa-tags"></i> Управление тегами транзакции</h3>

        <form id="tagTransactionForm" method="post" action="${pageContext.request.contextPath}/tags">
            <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>">
            <input type="hidden" id="modalTransactionId" name="transactionId">
            <input type="hidden" id="modalTransactionType" name="transactionType">

            <div class="tag-selection">
                <h4>Выберите теги:</h4>
                <div class="tag-checkboxes">
                    <% if (!userTags.isEmpty()) { %>
                    <% for (Tag tag : userTags) { %>
                    <label class="tag-checkbox">
                        <input type="checkbox" name="tagIds" value="<%= tag.getId() %>">
                        <span class="tag-badge" style="background-color: <%= tag.getColor() != null ? tag.getColor() : "#6498d4" %>;">
                                <%= tag.getName() %>
                            </span>
                    </label>
                    <% } %>
                    <% } else { %>
                    <p class="no-tags">У вас пока нет тегов. Создайте теги в разделе "Управление тегами".</p>
                    <% } %>
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