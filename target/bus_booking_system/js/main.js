// main.js: Handles login, signup, and role-based redirection

// -------------------- LOGIN --------------------
function login(event) {
    event.preventDefault(); // prevent form submit

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    if (!email || !password) return alert("Email and password required!");

    const data = { email, password };

    fetch("user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(resp => {
        if (resp.role === "ADMIN") {
            alert("Welcome Admin!");
            window.location.href = "admin-dashboard.jsp";
        } else if (resp.role === "USER") {
            alert(`Welcome ${resp.name}!`);
            window.location.href = "userDashboard.jsp?userId=" + resp.id;
        } else {
            alert(resp.message || "Login failed!");
        }
    })
    .catch(err => console.error(err));
}

// -------------------- SIGNUP --------------------
function signup(event) {
    event.preventDefault();

    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    if (!name || !email || !password) return alert("All fields required!");

    const data = { name, email, password, role: "USER" };

    fetch("user", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(resp => {
        alert(resp.message);
        if (resp.message.includes("success")) {
            window.location.href = "signin.jsp";
        }
    })
    .catch(err => console.error(err));
}

// -------------------- LOGOUT --------------------
function logout() {
    // Clear session/local storage if implemented
    alert("Logged out successfully!");
    window.location.href = "signin.jsp";
}