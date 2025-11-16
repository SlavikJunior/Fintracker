<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FinTracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="auth-page">
<div class="hero">
    <jsp:include page="components/messages.jsp"/>

    <h1>FinTracker</h1>
    <p>Ваш умный помощник по учету расходов. Отслеживайте финансы легко и эффективно!</p>
    <div class="auth-buttons">
        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
            <i class="fas fa-sign-in-alt"></i> Войти
        </a>
        <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary">
            <i class="fas fa-user-plus"></i> Регистрация
        </a>
    </div>
</div>

<footer class="footer">
    <div class="footer-content">
        <span><i class="fas fa-code"></i> Сделано с ❤️ для ваших финансов</span>
        <span>•</span>
        <span>FinTracker © 2025</span>
    </div>
</footer>
</body>
</html>