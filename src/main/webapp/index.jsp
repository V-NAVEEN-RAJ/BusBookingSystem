<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bus Booking System - Sign In</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f7f8;
        }
        .login-container {
            width: 350px;
            margin: 80px auto;
            background: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0,0,0,0.2);
        }
        .login-container h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #333;
        }
        .login-container label {
            display: block;
            margin-bottom: 5px;
            color: #555;
        }
        .login-container input[type="text"],
        .login-container input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .login-container button {
            width: 100%;
            padding: 10px;
            background: #28a745;
            border: none;
            color: #fff;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
        }
        .login-container button:hover {
            background: #218838;
        }
        .login-container .signup-link {
            text-align: center;
            margin-top: 15px;
        }
        .login-container .signup-link a {
            text-decoration: none;
            color: #007bff;
        }
        .login-container .signup-link a:hover {
            text-decoration: underline;
        }
        .error-msg {
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>

<div class="login-container">
    <h2>Sign In 🚌</h2>
    
    <div id="error" class="error-msg"></div>

    <form id="loginForm" onsubmit="handleLogin(event)">
        <label for="email">Email</label>
        <input type="text" id="email" name="email" placeholder="Enter email" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" placeholder="Enter password" required>

        <button type="submit">Login</button>
    </form>

    <div class="signup-link">
        Don't have an account? <a href="<%= request.getContextPath() %>/register">Sign Up</a>
    </div>
</div>

<script>
function handleLogin(event) {
    event.preventDefault();
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch("<%= request.getContextPath() %>/user", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "email=" + encodeURIComponent(email) +
              "&password=" + encodeURIComponent(password)
    })
    .then(res => {
        if (!res.ok) throw new Error("HTTP error " + res.status);
        return res.text(); // plain text
    })
    .then(result => {
        if (result.trim() === "success") {
            window.location.href = "<%= request.getContextPath() %>/user-dashboard";
        } else {
            document.getElementById("error").innerText = "Invalid login credentials!";
        }
    })
    .catch(err => {
        console.error("Login error:", err);
        document.getElementById("error").innerText = "Server error. Try again.";
    });
}
</script>

</body>
</html>
