<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign In - Bus Booking System</title>
    <link rel="stylesheet" href="css/style.css">
    <script>
        function handleSignin(event) {
            event.preventDefault();

            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;

            const data = { email, password };

            fetch("user", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
            .then(res => res.json())
            .then(response => {
                console.log("Login response:", response);

                if (response.role === "ADMIN") {
                    alert("Welcome Admin!");

                    // Save user info
                    sessionStorage.setItem("userId", response.id);
                    sessionStorage.setItem("userName", response.name);

                    window.location.href = "adminDashboard.jsp?uid=" + response.id + "&uname=" + encodeURIComponent(response.name);
                } 
                else if (response.role === "USER") {
                    alert("Welcome User!");

                    // Save user info
                    sessionStorage.setItem("userId", response.id);
                    sessionStorage.setItem("userName", response.name);

                    window.location.href = "userDashboard.jsp?uid=" + response.id + "&uname=" + encodeURIComponent(response.name);
                } 
                else {
                    alert(response.message || "Invalid credentials");
                }
            })
            .catch(err => console.error("Login error:", err));
        }
    </script>
</head>
<body>
    <div class="container" style="text-align:center; margin-top:100px;">
        <h2>Sign In</h2>
        <form id="signinForm" onsubmit="handleSignin(event)">
            <input type="email" id="email" placeholder="Email" required><br><br>
            <input type="password" id="password" placeholder="Password" required><br><br>
            <button type="submit" style="padding:10px 20px;">Sign In</button>
        </form>
        <p style="margin-top:15px;">Don't have an account? <a href="signup.jsp">Sign Up</a></p>
    </div>
</body>
</html>