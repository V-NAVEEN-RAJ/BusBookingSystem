package com.naveenraj.bus_booking_system.model;

import java.time.LocalDate;

public class Bus {

    private int id;
    private String busNumber;
    private String source;
    private String destination;
    private LocalDate travelDate;
    private int totalSeats;
    private int availableSeats;

    // Default constructor
    public Bus() {}

    // Constructor for creating a new bus
    public Bus(String busNumber, String source, String destination, LocalDate travelDate, int totalSeats, int availableSeats) {
        this.busNumber = busNumber;
        this.source = source;
        this.destination = destination;
        this.travelDate = travelDate;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}