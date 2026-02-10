<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <script>
        const busUrl = "bus";
        const userUrl = "user";

        // ---------------- BUS FUNCTIONS ----------------
        async function loadBuses() {
            try {
                const res = await fetch(busUrl);
                const buses = await res.json();
                let html = `<table class="table-auto w-full border-collapse border border-gray-300 mt-4">
                    <thead>
                        <tr class="bg-gray-200">
                            <th class="border px-2 py-1">ID</th>
                            <th class="border px-2 py-1">Bus Number</th>
                            <th class="border px-2 py-1">Source</th>
                            <th class="border px-2 py-1">Destination</th>
                            <th class="border px-2 py-1">Travel Date</th>
                            <th class="border px-2 py-1">Total Seats</th>
                            <th class="border px-2 py-1">Available Seats</th>
                            <th class="border px-2 py-1">Action</th>
                        </tr>
                    </thead><tbody>`;
                buses.forEach(bus => {
                    html += `<tr>
                        <td class="border px-2 py-1">${bus.id}</td>
                        <td class="border px-2 py-1">${bus.busNumber}</td>
                        <td class="border px-2 py-1">${bus.source}</td>
                        <td class="border px-2 py-1">${bus.destination}</td>
                        <td class="border px-2 py-1">${bus.travelDate}</td>
                        <td class="border px-2 py-1">${bus.totalSeats}</td>
                        <td class="border px-2 py-1">${bus.availableSeats}</td>
                        <td class="border px-2 py-1">
                            <button class="bg-red-500 text-white px-2 py-1 rounded" onclick="deleteBus(${bus.id})">Delete</button>
                        </td>
                    </tr>`;
                });
                html += `</tbody></table>`;
                document.getElementById("busContent").innerHTML = html;
            } catch(e){ console.error(e); }
        }

        async function addBus() {
            const busNumber = document.getElementById("busNumber").value;
            const source = document.getElementById("source").value;
            const destination = document.getElementById("destination").value;
            const travelDate = document.getElementById("travelDate").value;
            const totalSeats = parseInt(document.getElementById("totalSeats").value);

            if(!busNumber || !source || !destination || !travelDate || !totalSeats){
                alert("All fields required");
                return;
            }

            const data = { action:"add", busNumber, source, destination, travelDate, totalSeats };
            try{
                const res = await fetch(busUrl,{
                    method:"POST",
                    headers:{"Content-Type":"application/json"},
                    body:JSON.stringify(data)
                });
                const resp = await res.json();
                alert(resp.message);
                document.getElementById("addBusForm").classList.add("hidden");
                loadBuses();
            } catch(e){ console.error(e); }
        }

        function showAddBusForm() {
            document.getElementById("addBusForm").classList.remove("hidden");
        }

        async function deleteBus(id){
            if(!confirm("Delete this bus?")) return;
            try{
                const res = await fetch(`${busUrl}?id=${id}`,{method:"DELETE"});
                const resp = await res.json();
                alert(resp.message);
                loadBuses();
            }catch(e){ console.error(e); }
        }

        // ---------------- USER FUNCTIONS ----------------
        async function loadUsers(){
            try{
                const res = await fetch(userUrl);
                const users = await res.json();
                let html = `<table class="table-auto w-full border-collapse border border-gray-300 mt-4">
                    <thead>
                        <tr class="bg-gray-200">
                            <th class="border px-2 py-1">ID</th>
                            <th class="border px-2 py-1">Name</th>
                            <th class="border px-2 py-1">Email</th>
                            <th class="border px-2 py-1">Role</th>
                            <th class="border px-2 py-1">Created At</th>
                            <th class="border px-2 py-1">Action</th>
                        </tr>
                    </thead><tbody>`;
                users.forEach(u=>{
                    html+=`<tr>
                        <td class="border px-2 py-1">${u.id}</td>
                        <td class="border px-2 py-1">${u.name}</td>
                        <td class="border px-2 py-1">${u.email}</td>
                        <td class="border px-2 py-1">${u.role}</td>
                        <td class="border px-2 py-1">${u.createdAt}</td>
                        <td class="border px-2 py-1">
                            <button class="bg-red-500 text-white px-2 py-1 rounded" onclick="deleteUser(${u.id})">Delete</button>
                        </td>
                    </tr>`;
                });
                html += `</tbody></table>`;
                document.getElementById("userContent").innerHTML = html;
            }catch(e){ console.error(e); }
        }

        async function deleteUser(id){
            if(!confirm("Delete this user?")) return;
            try{
                const res = await fetch(`${userUrl}?id=${id}`,{method:"DELETE"});
                const resp = await res.json();
                alert(resp.message);
                loadUsers();
            }catch(e){ console.error(e); }
        }

        async function addUser(){
            const name = document.getElementById("userName").value;
            const email = document.getElementById("userEmail").value;
            const password = document.getElementById("userPassword").value;
            const role = document.getElementById("userRole").value || "USER";

            if(!name||!email||!password){ alert("All fields required"); return; }

            const data = {name,email,password,role};
            try{
                const res = await fetch(userUrl,{method:"POST", headers:{"Content-Type":"application/json"}, body:JSON.stringify(data)});
                const resp = await res.json();
                alert(resp.message);
                document.getElementById("addUserForm").classList.add("hidden");
                loadUsers();
            }catch(e){ console.error(e); }
        }

        function showAddUserForm() {
            document.getElementById("addUserForm").classList.remove("hidden");
        }

        function showTab(tab){
            document.getElementById("busTab").classList.add("hidden");
            document.getElementById("userTab").classList.add("hidden");
            document.getElementById(tab).classList.remove("hidden");
        }

        window.onload = function(){
            showTab("busTab");
            loadBuses();
            loadUsers();
        }
    </script>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50">
    <!-- Navbar -->
    <nav class="bg-blue-600 text-white p-4 flex justify-between">
        <div class="font-bold text-xl">Admin Dashboard</div>
        <div class="space-x-4">
            <button class="px-3 py-1 bg-blue-500 rounded hover:bg-blue-700" onclick="showTab('busTab')">Bus Management</button>
            <button class="px-3 py-1 bg-blue-500 rounded hover:bg-blue-700" onclick="showTab('userTab')">User Management</button>
            <button class="px-3 py-1 bg-red-500 rounded hover:bg-red-700" onclick="window.location.href='signin.jsp'">Logout</button>
        </div>
    </nav>

    <div class="p-6">
        <!-- Bus Management Tab -->
        <div id="busTab">
            <h2 class="text-2xl font-bold mb-4">Bus Management</h2>
            <div class="mb-4 space-x-2">
                <button class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700" onclick="showAddBusForm()">New Bus</button>
                <button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700" onclick="loadBuses()">Refresh</button>
            </div>
            <!-- Add Bus Form Hidden Initially -->
            <div id="addBusForm" class="hidden mb-4 space-x-2">
                <input type="text" id="busNumber" placeholder="Bus Number" class="border p-2 rounded">
                <input type="text" id="source" placeholder="Source" class="border p-2 rounded">
                <input type="text" id="destination" placeholder="Destination" class="border p-2 rounded">
                <input type="date" id="travelDate" class="border p-2 rounded">
                <input type="number" id="totalSeats" placeholder="Total Seats" class="border p-2 rounded">
                <button class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-800" onclick="addBus()">Submit</button>
            </div>
            <div id="busContent"></div>
        </div>

        <!-- User Management Tab -->
        <div id="userTab" class="hidden">
            <h2 class="text-2xl font-bold mb-4">User Management</h2>
            <div class="mb-4 space-x-2">
                <button class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700" onclick="showAddUserForm()">New User</button>
                <button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700" onclick="loadUsers()">Refresh</button>
            </div>
            <!-- Add User Form Hidden Initially -->
            <div id="addUserForm" class="hidden mb-4 space-x-2">
                <input type="text" id="userName" placeholder="Name" class="border p-2 rounded">
                <input type="email" id="userEmail" placeholder="Email" class="border p-2 rounded">
                <input type="password" id="userPassword" placeholder="Password" class="border p-2 rounded">
                <input type="text" id="userRole" placeholder="Role (optional)" class="border p-2 rounded">
                <button class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-800" onclick="addUser()">Submit</button>
            </div>
            <div id="userContent"></div>
        </div>
    </div>
</body>
</html>