<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Ошибка регистрации</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="auth-container">
  <h2><i class="fas fa-exclamation-circle"></i> Ошибка регистрации</h2>
  <div class="error-message">Возможно, пользователь с таким логином или email уже существует.</div>
  <a href="${pageContext.request.contextPath}/auth/register" class="btn btn-primary"><i class="fas fa-redo"></i> Попробовать снова</a>
</div>
</body>
</html>