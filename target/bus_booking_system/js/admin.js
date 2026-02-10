// admin.js: for admin-dashboard.jsp

function addBus() {
    const busNumber = prompt("Enter Bus Number:");
    const source = prompt("Enter Source:");
    const destination = prompt("Enter Destination:");
    const travelDate = prompt("Enter Travel Date (YYYY-MM-DD):");
    const totalSeats = prompt("Enter Total Seats:");

    if (!busNumber || !source || !destination || !travelDate || !totalSeats) {
        return alert("All fields are required!");
    }

    const data = {
        action: "add",
        busNumber: busNumber,
        source: source,
        destination: destination,
        travelDate: travelDate,
        totalSeats: parseInt(totalSeats)
    };

    fetch("bus", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(resp => alert(resp.message))
    .catch(err => console.error(err));
}

function viewUsers() {
    fetch("user")
        .then(res => res.json())
        .then(users => {
            let output = "All Users:\n";
            users.forEach(u => {
                output += `ID: ${u.id}, Name: ${u.name}, Email: ${u.email}, Role: ${u.role}\n`;
            });
            alert(output);
        })
        .catch(err => console.error(err));
}

function deleteBus() {
    const busId = prompt("Enter Bus ID to delete:");
    if (!busId) return;

    fetch(`bus?id=${busId}`, { method: "DELETE" })
        .then(res => res.json())
        .then(resp => alert(resp.message))
        .catch(err => console.error(err));
}

function deleteUser() {
    const userId = prompt("Enter User ID to delete:");
    if (!userId) return;

    fetch(`user?id=${userId}`, { method: "DELETE" })
        .then(res => res.json())
        .then(resp => alert(resp.message))
        .catch(err => console.error(err));
}