<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%
    String filterType = request.getParameter("filterType");
    String filterCategory = request.getParameter("filterCategory");
    String filterTag = request.getParameter("filterTag");
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");

    String allCategoriesStr = request.getParameter("allCategories");
    String userTagNamesStr = request.getParameter("userTagNames");

    List<String> allCategories = Arrays.asList(allCategoriesStr.split(","));
    List<String> userTagNames = Arrays.asList(userTagNamesStr.split(","));
%>

<div class="filters-section">
    <h3><i class="fas fa-filter"></i> Фильтры</h3>
    <form method="get" action="${pageContext.request.contextPath}/main">
        <div class="filter-row">
            <div class="filter-group">
                <label for="typeFilter">Тип:</label>
                <select id="typeFilter" name="type">
                    <option value="">Все</option>
                    <option value="INCOME" <%= "INCOME".equals(filterType) ? "selected" : "" %>>Доходы</option>
                    <option value="EXPENSE" <%= "EXPENSE".equals(filterType) ? "selected" : "" %>>Расходы</option>
                </select>
            </div>
            <div class="filter-group">
                <label for="categoryFilter">Категория:</label>
                <select id="categoryFilter" name="category">
                    <option value="">Все категории</option>
                    <% for (String category : allCategories) {
                        if (!category.isEmpty()) { %>
                    <option value="<%= category %>" <%= category.equals(filterCategory) ? "selected" : "" %>>
                        <%= category %>
                    </option>
                    <% } } %>
                </select>
            </div>
            <div class="filter-group">
                <label for="tagFilter">Тег:</label>
                <select id="tagFilter" name="tag">
                    <option value="">Все теги</option>
                    <% for (String tagName : userTagNames) {
                        if (!tagName.isEmpty()) { %>
                    <option value="<%= tagName %>" <%= tagName.equals(filterTag) ? "selected" : "" %>>
                        <%= tagName %>
                    </option>
                    <% } } %>
                </select>
            </div>
            <div class="filter-group">
                <label for="startDate">С:</label>
                <input type="date" id="startDate" name="startDate" value="<%= startDate != null ? startDate : "" %>">
            </div>
            <div class="filter-group">
                <label for="endDate">По:</label>
                <input type="date" id="endDate" name="endDate" value="<%= endDate != null ? endDate : "" %>">
            </div>
            <div class="filter-actions">
                <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Применить</button>
                <a href="${pageContext.request.contextPath}/main" class="btn btn-secondary"><i class="fas fa-times"></i> Сбросить</a>
            </div>
        </div>
    </form>
</div>