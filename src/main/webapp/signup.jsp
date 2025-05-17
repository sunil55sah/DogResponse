<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Sign Up</title>
    <link rel="stylesheet" href="css/loginANDregister.css">
</head>
<body>
    <h2>Sign Up</h2>
    <form action="signup" method="post">
        Email: <input type="email" name="email" required /><br/>
        Password: <input type="password" name="password" required /><br/>
        <button type="submit">Register</button>
    </form>
    <p>Already have an account? <a href="login.jsp">Login here</a></p>
</body>
</html>
