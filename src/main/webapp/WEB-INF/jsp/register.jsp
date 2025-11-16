<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация в FinTracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="auth-page">
<div class="auth-container">
    <h2><i class="fas fa-user-plus"></i> Регистрация</h2>

    <jsp:include page="components/messages.jsp"/>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
        <div class="form-group">
            <label for="login"><i class="fas fa-user"></i> Логин</label>
            <input type="text" id="login" name="login" placeholder="Введите логин" required>
        </div>
        <div class="form-group">
            <label for="password"><i class="fas fa-key"></i> Пароль</label>
            <input type="password" id="password" name="password" placeholder="Введите пароль" required>
        </div>
        <div class="form-group">
            <label for="email"><i class="fas fa-envelope"></i> Email</label>
            <input type="email" id="email" name="email" placeholder="Введите email" required>
        </div>
        <button type="submit" class="btn btn-primary"><i class="fas fa-check"></i> Зарегистрироваться</button>
    </form>
    <a href="${pageContext.request.contextPath}/auth" class="back-link btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Назад
    </a>
</div>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>