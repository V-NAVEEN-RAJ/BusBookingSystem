package com.naveenraj.bus_booking_system.controller;

import com.naveenraj.bus_booking_system.dao.BusDao;
import com.naveenraj.bus_booking_system.model.Bus;
import com.google.gson.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/bus")
public class BusController extends HttpServlet {

    private BusDao busDao = new BusDao();

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.toString());
                }
            })
            .create();

    // GET all buses
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        try {
            List<Bus> buses = busDao.getAllBuses();
            resp.getWriter().write(gson.toJson(buses));
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write(gson.toJson(new ResponseMessage("Error: " + e.getMessage())));
        }
    }

    // POST add new bus
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        BusRequest busRequest = gson.fromJson(sb.toString(), BusRequest.class);

        if (busRequest.getAction() != null && busRequest.getAction().equalsIgnoreCase("add")) {
            Bus bus = new Bus(
                    busRequest.getBusNumber(),
                    busRequest.getSource(),
                    busRequest.getDestination(),
                    LocalDate.parse(busRequest.getTravelDate()),
                    busRequest.getTotalSeats(),
                    busRequest.getTotalSeats()
            );

            boolean success = busDao.addBus(bus);
            if (success)
                resp.getWriter().write(gson.toJson(new ResponseMessage("Bus added successfully")));
            else
                resp.getWriter().write(gson.toJson(new ResponseMessage("Failed to add bus")));
        } else {
            resp.getWriter().write(gson.toJson(new ResponseMessage("Invalid action")));
        }
    }

    // DELETE bus
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.getWriter().write(gson.toJson(new ResponseMessage("Bus ID required")));
            return;
        }

        int id = Integer.parseInt(idParam);
        boolean success = busDao.deleteBus(id);

        if (success)
            resp.getWriter().write(gson.toJson(new ResponseMessage("Bus deleted successfully")));
        else
            resp.getWriter().write(gson.toJson(new ResponseMessage("Failed to delete bus")));
    }

    private static class BusRequest {
        private String action;
        private String busNumber;
        private String source;
        private String destination;
        private String travelDate;
        private int totalSeats;

        public String getAction() { return action; }
        public String getBusNumber() { return busNumber; }
        public String getSource() { return source; }
        public String getDestination() { return destination; }
        public String getTravelDate() { return travelDate; }
        public int getTotalSeats() { return totalSeats; }
    }

    private static class ResponseMessage {
        private String message;
        public ResponseMessage(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}