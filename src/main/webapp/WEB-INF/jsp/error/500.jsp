<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>500 - Внутренняя ошибка</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="error-page">
  <h1><i class="fas fa-exclamation-triangle"></i> 500</h1>
  <p>Внутренняя ошибка сервера. Наши инженеры уже в курсе и работают над этим!</p>
  <a href="${pageContext.request.contextPath}/auth" class="btn btn-primary"><i class="fas fa-home"></i> На главную</a>
</div>
<footer class="footer">
  <div class="footer-content">
    <span>AI FinTracker © 2025</span>
    <span>•</span>
    <span><a href="${pageContext.request.contextPath}/auth">На главную</a></span>
  </div>
</footer>
</body>
</html>