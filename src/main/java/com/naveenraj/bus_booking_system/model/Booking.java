package com.naveenraj.bus_booking_system.model;

import java.sql.Timestamp;

public class Booking {

    private int id;
    private int busId;
    private int userId;
    private int seatsBooked;
    private Timestamp bookingDate; // ✅ Use Timestamp

    // Default constructor
    public Booking() {}

    // Constructor for creating a new booking
    public Booking(int busId, int userId, int seatsBooked) {
        this.busId = busId;
        this.userId = userId;
        this.seatsBooked = seatsBooked;
    }

    // Constructor for reading from DB
    public Booking(int id, int busId, int userId, int seatsBooked, Timestamp bookingDate) {
        this.id = id;
        this.busId = busId;
        this.userId = userId;
        this.seatsBooked = seatsBooked;
        this.bookingDate = bookingDate;
    }

    // Getters
    public int getId() { return id; }
    public int getBusId() { return busId; }
    public int getUserId() { return userId; }
    public int getSeatsBooked() { return seatsBooked; }
    public Timestamp getBookingDate() { return bookingDate; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setBusId(int busId) { this.busId = busId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setSeatsBooked(int seatsBooked) { this.seatsBooked = seatsBooked; }
    public void setBookingDate(Timestamp bookingDate) { this.bookingDate = bookingDate; } // ✅ Timestamp
}