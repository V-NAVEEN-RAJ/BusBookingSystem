package com.naveenraj.bus_booking_system.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.naveenraj.bus_booking_system.dao.BookingDao;
import com.naveenraj.bus_booking_system.model.Booking;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/booking")
public class BookingController extends HttpServlet {

    private BookingDao bookingDao = new BookingDao();
    private Gson gson;

    @Override
    public void init() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        gson = builder.create();
    }

    // -------------------- GET BOOKINGS --------------------
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        String userIdParam = req.getParameter("userId");

        try {
            if (userIdParam == null) {
                // Return all bookings
                List<Booking> bookings = bookingDao.getAllBookings();
                resp.getWriter().write(gson.toJson(bookings));
            } else {
                int userId = Integer.parseInt(userIdParam);
                List<Booking> bookings = bookingDao.getBookingsByUserId(userId);
                resp.getWriter().write(gson.toJson(bookings));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ResponseMessage("Error: " + e.getMessage())));
        }
    }

    // -------------------- CREATE BOOKING --------------------
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        try {
            // Read JSON from request body
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Booking booking = gson.fromJson(sb.toString(), Booking.class);

            // Validate data
            if (booking.getUserId() <= 0 || booking.getBusId() <= 0 || booking.getSeatsBooked() <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ResponseMessage("Invalid booking data")));
                return;
            }

            // Attempt to create booking
            boolean success = bookingDao.createBooking(booking);

            if (success) {
                resp.getWriter().write(gson.toJson(new ResponseMessage("Booking successful")));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(gson.toJson(new ResponseMessage(
                        "Booking failed: Not enough available seats or invalid bus"
                )));
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ResponseMessage("Booking failed: " + e.getMessage())));
        }
    }

    // -------------------- RESPONSE MESSAGE --------------------
    private static class ResponseMessage {
        private String message;
        public ResponseMessage(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}