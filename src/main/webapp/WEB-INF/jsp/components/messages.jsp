<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${not empty successMessage}">
    <div class="success-message">
        <i class="fas fa-check-circle"></i> ${successMessage}
    </div>
</c:if>

<c:if test="${not empty errorMessage}">
    <div class="error-message">
        <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
    </div>
</c:if>