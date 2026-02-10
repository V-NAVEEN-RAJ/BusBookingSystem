<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign In - Bus Booking System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        body { font-family: Arial; background: #f4f7f8; }
        .login-container {
            width: 350px; margin: 80px auto; background: #fff; padding: 30px;
            border-radius: 10px; box-shadow: 0 0 15px rgba(0,0,0,0.2);
        }
        .login-container h2 { text-align:center; margin-bottom:25px; color:#333; }
        input, select { width:100%; padding:10px; margin-bottom:15px; border-radius:5px; border:1px solid #ccc; }
        button { width:100%; padding:10px; background:#28a745; border:none; color:#fff; font-size:16px; border-radius:5px; cursor:pointer; }
        button:hover { background:#218838; }
        .link { text-align:center; margin-top:15px; }
        .link a { color:#007bff; text-decoration:none; }
        .link a:hover { text-decoration:underline; }
        .error-msg { color:red; text-align:center; margin-bottom:10px; }
    </style>
</head>
<body>

<div class="login-container">
    <h2>Sign In 🚌</h2>
    <div id="error" class="error-msg"></div>

    <form id="signinForm" onsubmit="handleSignin(event)">
        <input type="email" id="email" placeholder="Email" required>
        <input type="password" id="password" placeholder="Password" required>
        <button type="submit">Sign In</button>
    </form>

    <div class="link">
        Don't have an account? 
        <a href="<%= request.getContextPath() %>/register">Sign Up</a>
    </div>
</div>

<script>
function handleSignin(event) {
    event.preventDefault();
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch("<%= request.getContextPath() %>/user", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: "email=" + encodeURIComponent(email) + "&password=" + encodeURIComponent(password)
    })
    .then(res => {
        if (!res.ok) throw new Error("HTTP " + res.status);
        return res.text();
    })
    .then(result => {
        if (result.trim() === "success") {
            window.location.href = "<%= request.getContextPath() %>/user-dashboard";
        } else if (result.trim() === "admin") {
            window.location.href = "<%= request.getContextPath() %>/admin-dashboard";
        } else {
            document.getElementById("error").innerText = "Invalid login credentials!";
        }
    })
    .catch(err => {
        console.error(err);
        document.getElementById("error").innerText = "Server error. Try again.";
    });
}
</script>

</body>
</html>
