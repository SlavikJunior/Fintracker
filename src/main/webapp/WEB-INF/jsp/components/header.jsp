<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="header">
  <h1><i class="fas fa-wallet"></i>FinTracker</h1>
  <p>Добро пожаловать! Ваш помощник по учету расходов</p>
  <div class="user-info">
    <span><i class="fas fa-user-circle"></i> Пользователь: <%= session.getAttribute("user_login") %></span>
    <a href="${pageContext.request.contextPath}/logout" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Выйти</a>
  </div>
</header>