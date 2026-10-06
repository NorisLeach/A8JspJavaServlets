<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.snake.model.User" %>
<html>
<head>
    <title>Snake Game - Arena</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/game.css">
</head>
<body>

<%
    User loggedInUser = (User) session.getAttribute("currentUser");
%>

<div class="welcome-container">
    <h1>Salut, <%= loggedInUser.getUsername() %>! 🐍</h1>
    <p>Folosește săgețile de la tastatură pentru a controla șarpele.</p>

    <canvas id="snakeCanvas" width="400" height="400"></canvas>

    <br>
    <a href="logout" class="logout-btn">Deconectare</a>
</div>

<script src="${pageContext.request.contextPath}/js/game.js"></script>

</body>
</html>