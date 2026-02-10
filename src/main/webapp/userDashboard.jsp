<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    String contextPath = request.getContextPath();

    // Read from URL if coming from login redirect like: userDashboard.jsp?uid=5&uname=Raj
    String uidParam = request.getParameter("uid");
    String unameParam = request.getParameter("uname");

    if (uidParam != null) {
        try {
            session.setAttribute("userId", Integer.parseInt(uidParam));
        } catch (Exception e) {
            // ignore
        }
    }

    if (unameParam != null) {
        session.setAttribute("userName", unameParam);
    }

    Integer uid = (Integer) session.getAttribute("userId");
    String uname = (String) session.getAttribute("userName");

    // If still not logged in → go to signin
    if (uid == null) {
        response.sendRedirect(contextPath + "/signin.jsp");
        return;
    }

    String safeUserName = (uname != null) ? uname.replace("'", "\\'") : "User";
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Dashboard</title>
    <link rel="stylesheet" href="<%= contextPath %>/css/style.css">
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #eef2f3; margin: 0; }
        header { background: #007BFF; color: white; padding: 20px; display: flex; justify-content: space-between; align-items: center; }
        nav { background: #333; padding: 10px; display: flex; justify-content: center; }
        nav button { margin: 0 10px; padding: 10px 20px; border: none; border-radius: 5px; color: white; background: #007BFF; cursor: pointer; transition: 0.3s; }
        nav button:hover { background: #0056b3; }
        .container { max-width: 1200px; margin: 20px auto; }
        table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        th, td { padding: 12px; text-align: center; border-bottom: 1px solid #ddd; }
        th { background-color: #007BFF; color: white; }
        tr:hover { background-color: #f1f1f1; }
        button.reserveBtn { background-color: #28a745; }
        button.reserveBtn:hover { background-color: #218838; }
        input[type=number] { width: 60px; text-align: center; }
        h2 { color: #333; margin-bottom: 10px; }
        .booking-container { display: flex; flex-direction: column; align-items: center; }
        .booking-container table { margin-bottom: 20px; }
        .booking-actions button { margin-left: 5px; }
    </style>
</head>
<body>

<header>
    <h1>Welcome, <span id="userName"></span>!</h1>
    <button onclick="logout()">Logout</button>
</header>

<nav class="container">
    <button onclick="showBuses()">View Available Buses</button>
    <button onclick="viewHistory()">Booking History</button>
</nav>

<div id="busesDiv" class="container"></div>
<div id="historyDiv" class="container"></div>

<script>
const contextPath = "<%= contextPath %>";
const userId = parseInt("<%= (uid != null) ? uid : 0 %>", 10);
const userName = "<%= safeUserName %>";

document.getElementById("userName").innerText = userName;

function logout() {
    alert("Logged out successfully!");
    window.location.href = contextPath + "/signin.jsp";
}

function showBuses() {
    document.getElementById("historyDiv").innerHTML = "";
    fetch(contextPath + "/bus")
        .then(res => res.json())
        .then(buses => {
            if (!buses || buses.length === 0) {
                document.getElementById("busesDiv").innerHTML = "<p>No buses available.</p>";
                return;
            }

            let html = `<h2>Available Buses</h2>
            <div class="booking-container">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Bus Number</th>
                    <th>Source</th>
                    <th>Destination</th>
                    <th>Travel Date</th>
                    <th>Total Seats</th>
                    <th>Available Seats</th>
                    <th>Seats to Book</th>
                    <th>Action</th>
                </tr>`;

            buses.forEach(bus => {
                html += `<tr data-bus-id="${bus.id}">
                    <td>${bus.id}</td>
                    <td>${bus.busNumber}</td>
                    <td>${bus.source}</td>
                    <td>${bus.destination}</td>
                    <td>${bus.travelDate}</td>
                    <td>${bus.totalSeats}</td>
                    <td class="availableSeats">${bus.availableSeats}</td>
                    <td>
                        <input type="number" id="seats_${bus.id}" min="1" max="${bus.availableSeats}" placeholder="0">
                    </td>
                    <td>
                        <button class="reserveBtn" onclick="reserveBus(${bus.id}, this)">Reserve</button>
                    </td>
                </tr>`;
            });

            html += `</table></div>`;
            document.getElementById("busesDiv").innerHTML = html;
        })
        .catch(err => {
            console.error(err);
            document.getElementById("busesDiv").innerHTML = "<p>Error fetching buses!</p>";
        });
}

function reserveBus(busId, btn) {
    const seatsInput = document.getElementById(`seats_${busId}`);
    let seats = parseInt(seatsInput.value);

    if (isNaN(seats) || seats <= 0) {
        alert("Enter valid number of seats!");
        return;
    }

    const availableSeatsCell = btn.closest("tr").querySelector(".availableSeats");
    const availableSeats = parseInt(availableSeatsCell.innerText);

    if (seats > availableSeats) {
        alert("Not enough seats available!");
        return;
    }

    const bookingData = { userId, busId, seatsBooked: seats };

    fetch(contextPath + "/booking", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(bookingData)
    })
    .then(res => res.json())
    .then(resp => {
        alert(resp.message);
        if (resp.message === "Booking successful") {
            seatsInput.value = "";
            showBuses();
            viewHistory();
        }
    })
    .catch(err => alert("Error: " + err.message));
}

function viewHistory() {
    document.getElementById("busesDiv").innerHTML = "";
    fetch(`${contextPath}/booking?userId=${userId}`)
        .then(res => res.json())
        .then(bookings => {
            if (!bookings || bookings.length === 0) {
                document.getElementById("historyDiv").innerHTML = "<p>No bookings found!</p>";
                return;
            }

            let html = `<h2>Your Bookings</h2>
            <table>
                <tr>
                    <th>Booking ID</th>
                    <th>Bus ID</th>
                    <th>Seats Booked</th>
                    <th>Booking Date</th>
                </tr>`;

            bookings.forEach(b => {
                html += `<tr>
                    <td>${b.id}</td>
                    <td>${b.busId}</td>
                    <td>${b.seatsBooked}</td>
                    <td>${b.bookingDate}</td>
                </tr>`;
            });

            html += `</table>`;
            document.getElementById("historyDiv").innerHTML = html;
        })
        .catch(err => console.error(err));
}

showBuses();
</script>
</body>
</html>
