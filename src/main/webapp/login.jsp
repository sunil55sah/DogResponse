<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/loginANDregister.css">
</head>
<body>
    <h2>Login</h2>
    <form action="login" method="post">
        Email: <input type="email" name="email" required /><br/>
        Password: <input type="password" name="password" required /><br/>
        <button type="submit">Login</button>
    </form>
    <p>Don't have an account? <a href="signup.jsp">Sign up here</a></p>
</body>
</html>
