package com.naveenraj.bus_booking_system.dao;

import com.naveenraj.bus_booking_system.model.Bus;
import com.naveenraj.bus_booking_system.utils.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BusDao {

    // Add a new bus
    public boolean addBus(Bus bus) {
        String sql = "INSERT INTO buses (bus_number, source, destination, travel_date, total_seats, available_seats) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bus.getBusNumber());
            ps.setString(2, bus.getSource());
            ps.setString(3, bus.getDestination());
            ps.setDate(4, Date.valueOf(bus.getTravelDate()));
            ps.setInt(5, bus.getTotalSeats());
            ps.setInt(6, bus.getAvailableSeats());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all buses
    public List<Bus> getAllBuses() {
        List<Bus> buses = new ArrayList<>();
        String sql = "SELECT id, bus_number, source, destination, travel_date, total_seats, available_seats FROM buses ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bus b = new Bus();
                b.setId(rs.getInt("id"));
                b.setBusNumber(rs.getString("bus_number"));
                b.setSource(rs.getString("source"));
                b.setDestination(rs.getString("destination"));
                b.setTravelDate(rs.getDate("travel_date").toLocalDate());
                b.setTotalSeats(rs.getInt("total_seats"));
                b.setAvailableSeats(rs.getInt("available_seats"));

                buses.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buses;
    }

    // Delete bus by ID
    public boolean deleteBus(int id) {
        String sql = "DELETE FROM buses WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}