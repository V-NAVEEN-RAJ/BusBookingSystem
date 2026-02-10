package com.naveenraj.bus_booking_system.controller;

import com.google.gson.Gson;
import com.naveenraj.bus_booking_system.dao.BusDao;
import com.naveenraj.bus_booking_system.model.Bus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
public class AdminController extends HttpServlet {

    private BusDao busDao = new BusDao();
    private Gson gson = new Gson();

    // ------------------- GET (URL params) -------------------
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        String action = req.getParameter("action");

        if ("listBus".equalsIgnoreCase(action)) {
            List<Bus> buses = busDao.getAllBuses();
            resp.getWriter().write(gson.toJson(buses));
        } else {
            resp.getWriter().write(gson.toJson(new Message("Invalid or missing action")));
        }
    }

    // ------------------- POST (JSON or Form) -------------------
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        String action = req.getParameter("action");

        String busNumber = req.getParameter("busNumber");
        String source = req.getParameter("source");
        String destination = req.getParameter("destination");
        String time = req.getParameter("time");
        String seatsStr = req.getParameter("totalSeats");
        String idStr = req.getParameter("id");

        // If not form data, try JSON
        if (action == null) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            String body = sb.toString().trim();
            if (!body.isEmpty()) {
                AdminRequest ar = gson.fromJson(body, AdminRequest.class);
                if (ar != null) {
                    action = ar.action;
                    busNumber = ar.busNumber;
                    source = ar.source;
                    destination = ar.destination;
                    time = ar.time;
                    if (ar.totalSeats != null) seatsStr = String.valueOf(ar.totalSeats);
                    if (ar.id != null) idStr = String.valueOf(ar.id);
                }
            }
        }

        if (action == null) {
            resp.getWriter().write(gson.toJson(new Message("Missing action")));
            return;
        }

        switch (action.toLowerCase()) {
            case "addbus":
                handleAddBus(busNumber, source, destination, time, seatsStr, resp);
                break;

            case "deletebus":
                handleDeleteBus(idStr, resp);
                break;

            default:
                resp.getWriter().write(gson.toJson(new Message("Invalid action")));
        }
    }

    // ------------------- HANDLERS -------------------

    private void handleAddBus(String busNumber, String source, String destination, String time, String seatsStr, HttpServletResponse resp) throws IOException {

        if (busNumber == null || source == null || destination == null || time == null || seatsStr == null) {
            resp.getWriter().write(gson.toJson(new Message("Missing fields")));
            return;
        }

        int totalSeats;
        try {
            totalSeats = Integer.parseInt(seatsStr);
        } catch (NumberFormatException e) {
            resp.getWriter().write(gson.toJson(new Message("Invalid seats number")));
            return;
        }

        Bus bus = new Bus();
        bus.setBusNumber(busNumber);
        bus.setSource(source);
        bus.setDestination(destination);
        // bus.setDepartureTime(time);
        bus.setTotalSeats(totalSeats);
        bus.setAvailableSeats(totalSeats);

        boolean success = busDao.addBus(bus);

        if (success) {
            resp.getWriter().write(gson.toJson(new Message("Bus added successfully")));
        } else {
            resp.getWriter().write(gson.toJson(new Message("Failed to add bus")));
        }
    }

    private void handleDeleteBus(String idStr, HttpServletResponse resp) throws IOException {
        if (idStr == null) {
            resp.getWriter().write(gson.toJson(new Message("Missing bus id")));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.getWriter().write(gson.toJson(new Message("Invalid bus id")));
            return;
        }

        boolean success = busDao.deleteBus(id);

        if (success) {
            resp.getWriter().write(gson.toJson(new Message("Bus deleted successfully")));
        } else {
            resp.getWriter().write(gson.toJson(new Message("Failed to delete bus")));
        }
    }

    // ------------------- INNER CLASSES -------------------

    private static class Message {
        String message;
        Message(String message) { this.message = message; }
    }

    private static class AdminRequest {
        String action;
        String busNumber;
        String source;
        String destination;
        String time;
        Integer totalSeats;
        Integer id;
    }
}