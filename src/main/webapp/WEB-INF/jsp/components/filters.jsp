<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="filterType" value="${requestScope.type}" />
<c:set var="filterCategory" value="${requestScope.category}" />
<c:set var="filterTag" value="${requestScope.tag}" />
<c:set var="startDate" value="${requestScope.startDate}" />
<c:set var="endDate" value="${requestScope.endDate}" />
<c:set var="allCategories" value="${requestScope.allCategories}" />
<c:if test="${empty allCategories}">
    <c:set var="allCategories" value="<%= java.util.Collections.emptyList() %>" />
</c:if>
<c:set var="userTagNames" value="${requestScope.userTagNames}" />
<c:if test="${empty userTagNames}">
    <c:set var="userTagNames" value="<%= java.util.Collections.emptyList() %>" />
</c:if>

<div class="filters-section">
    <h3><i class="fas fa-filter"></i> Фильтры</h3>
    <form method="get" action="${pageContext.request.contextPath}/main">
        <div class="filter-row">
            <div class="filter-group">
                <label for="typeFilter">Тип:</label>
                <select id="typeFilter" name="type">
                    <option value="">Все</option>
                    <option value="INCOME" ${filterType == 'INCOME' ? 'selected' : ''}>Доходы</option>
                    <option value="EXPENSE" ${filterType == 'EXPENSE' ? 'selected' : ''}>Расходы</option>
                </select>
            </div>
            <div class="filter-group">
                <label for="categoryFilter">Категория:</label>
                <select id="categoryFilter" name="category">
                    <option value="">Все категории</option>
                    <c:forEach items="${allCategories}" var="category">
                        <option value="${category}" ${category == filterCategory ? 'selected' : ''}>
                                ${category}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-group">
                <label for="tagFilter">Тег:</label>
                <select id="tagFilter" name="tag">
                    <option value="">Все теги</option>
                    <c:forEach items="${userTagNames}" var="tagName">
                        <option value="${tagName}" ${tagName == filterTag ? 'selected' : ''}>
                                ${tagName}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-group">
                <label for="startDate">С:</label>
                <input type="date" id="startDate" name="startDate" value="${startDate}">
            </div>
            <div class="filter-group">
                <label for="endDate">По:</label>
                <input type="date" id="endDate" name="endDate" value="${endDate}">
            </div>
            <div class="filter-actions">
                <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Применить</button>
                <a href="${pageContext.request.contextPath}/main" class="btn btn-secondary"><i class="fas fa-times"></i> Сбросить</a>
            </div>
        </div>
    </form>
</div>