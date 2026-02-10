package com.naveenraj.bus_booking_system.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Timestamp createdAt; // Using Timestamp

    // ------------------ DEFAULT ------------------
    public User() {}

    // ------------------ CONSTRUCTOR FOR DAO (with Timestamp) ------------------
    public User(int id, String name, String email, String password, String role, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    // ------------------ CONSTRUCTOR FOR DAO (with LocalDateTime) ------------------
    public User(int id, String name, String email, String password, String role, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = Timestamp.valueOf(createdAt); // convert LocalDateTime → Timestamp
    }

    // ------------------ CONSTRUCTOR FOR REGISTER ------------------
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // ------------------ GETTERS ------------------
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public Timestamp getCreatedAt() { return createdAt; }

    // ------------------ SETTERS ------------------
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}