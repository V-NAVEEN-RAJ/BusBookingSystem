<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign Up - Bus Booking System</title>
    <link rel="stylesheet" href="css/style.css">
    <script>
        function handleSignup(event) {
            event.preventDefault();

            const name = document.getElementById("name").value;
            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;
            const role = document.getElementById("role").value;

            const data = { name, email, password, role };

            fetch("user", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
            .then(res => res.json())
            .then(response => {
                alert(response.message);
                if (response.success) {
                    window.location.href = "signin.jsp";
                }
            })
            .catch(err => console.error(err));
        }
    </script>
</head>
<body>
    <div class="container" style="text-align:center; margin-top:100px;">
        <h2>Sign Up</h2>
        <form id="signupForm" onsubmit="handleSignup(event)">
            <input type="text" id="name" placeholder="Name" required><br><br>
            <input type="email" id="email" placeholder="Email" required><br><br>
            <input type="password" id="password" placeholder="Password" required><br><br>
            <select id="role" required>
                <option value="USER">User</option>
                <option value="ADMIN">Admin</option>
            </select><br><br>
            <button type="submit" style="padding:10px 20px;">Sign Up</button>
        </form>
        <p style="margin-top:15px;">Already have an account? <a href="signin.jsp">Sign In</a></p>
    </div>
</body>
</html>