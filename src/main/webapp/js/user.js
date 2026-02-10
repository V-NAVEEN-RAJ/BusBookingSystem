function showBuses() {
    document.getElementById("historyDiv").innerHTML = ""; // hide history table
    fetch("bus")
        .then(res => res.json())
        .then(buses => {
            const busesDiv = document.getElementById("busesDiv");
            if (!buses || buses.length === 0) {
                busesDiv.innerHTML = "<p>No buses available.</p>";
                return;
            }

            let html = "<h2>Available Buses</h2>";
            html += `<table>
                        <tr>
                            <th>ID</th>
                            <th>Bus Number</th>
                            <th>Source</th>
                            <th>Destination</th>
                            <th>Travel Date</th>
                            <th>Total Seats</th>
                            <th>Available Seats</th>
                            <th>Action</th>
                        </tr>`;

            buses.forEach(bus => {
                html += `<tr>
                            <td>${bus.id || ''}</td>
                            <td>${bus.busNumber || ''}</td>
                            <td>${bus.source || ''}</td>
                            <td>${bus.destination || ''}</td>
                            <td>${bus.travelDate || ''}</td>
                            <td>${bus.totalSeats || 0}</td>
                            <td>${bus.availableSeats || 0}</td>
                            <td>
                                <button class="reserveBtn" data-id="${bus.id}" data-seats="${bus.availableSeats || 0}">Reserve</button>
                            </td>
                         </tr>`;
            });

            html += "</table>";
            busesDiv.innerHTML = html;

            // Add click event listeners to all reserve buttons
            const buttons = document.querySelectorAll(".reserveBtn");
            buttons.forEach(btn => {
                btn.addEventListener("click", () => {
                    const id = btn.getAttribute("data-id");
                    const availableSeats = btn.getAttribute("data-seats");
                    reserveBus(id, availableSeats);
                });
            });
        })
        .catch(err => {
            console.error(err);
            document.getElementById("busesDiv").innerHTML = "<p>Error fetching buses!</p>";
        });
}