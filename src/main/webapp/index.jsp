<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Автоматический редирект на страницу аутентификации
    response.sendRedirect(request.getContextPath() + "/auth");
%>
<html>
<head>
    <title>Redirecting...</title>
</head>
<body>
<p>Redirecting to authentication page...</p>
<script>
    // JavaScript редирект на случай если серверный не сработает
    setTimeout(function() {
        window.location.href = "${pageContext.request.contextPath}/auth";
    }, 1000);
</script>
</body>
</html>