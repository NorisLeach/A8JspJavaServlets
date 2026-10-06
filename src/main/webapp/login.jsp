<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login - Snake Game</title>
    <link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>

<div class="login-container">
    <h2>Snake Login</h2>

    <% if (request.getAttribute("errorMessage") != null) { %>
    <div class="error-message">
        <%= request.getAttribute("errorMessage") %>
    </div>
    <% } %>

    <form action="login" method="POST">
        <input type="text" name="username" placeholder="Nume utilizator" required>
        <input type="password" name="password" placeholder="Parolă" required>

        <input type="submit" value="Intră în Joc">
    </form>
</div>

</body>
</html>