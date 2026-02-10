<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign Up - Bus Booking System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        body { font-family: Arial; background: #f4f7f8; }
        .signup-container {
            width: 350px; margin: 80px auto; background: #fff; padding: 30px;
            border-radius: 10px; box-shadow: 0 0 15px rgba(0,0,0,0.2);
        }
        h2 { text-align:center; margin-bottom:25px; color:#333; }
        input, select { width:100%; padding:10px; margin-bottom:15px; border-radius:5px; border:1px solid #ccc; }
        button { width:100%; padding:10px; background:#007bff; border:none; color:#fff; font-size:16px; border-radius:5px; cursor:pointer; }
        button:hover { background:#0056b3; }
        .link { text-align:center; margin-top:15px; }
        .link a { color:#007bff; text-decoration:none; }
        .link a:hover { text-decoration:underline; }
        .error-msg { color:red; text-align:center; margin-bottom:10px; }
    </style>
</head>
<body>

<div class="signup-container">
    <h2>Sign Up 🚌</h2>
    <div id="error" class="error-msg"></div>

    <form id="signupForm" onsubmit="handleSignup(event)">
        <input type="text" id="name" placeholder="Name" required>
        <input type="email" id="email" placeholder="Email" required>
        <input type="password" id="password" placeholder="Password" required>
        <select id="role" required>
            <option value="USER">User</option>
            <option value="ADMIN">Admin</option>
        </select>
        <button type="submit">Sign Up</button>
    </form>

    <div class="link">
        Already have an account? 
        <a href="<%= request.getContextPath() %>/login">Sign In</a>
    </div>
</div>

<s
