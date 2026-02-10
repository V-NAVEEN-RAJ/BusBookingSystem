package com.naveenraj.bus_booking_system.dao;

import com.naveenraj.bus_booking_system.model.Booking;
import com.naveenraj.bus_booking_system.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDao {

    // -------------------- ADD BOOKING --------------------
    public boolean addBooking(Booking booking) {
        String checkSeatsSql = "SELECT available_seats FROM buses WHERE id = ?";
        String insertBookingSql = "INSERT INTO bookings (bus_id, user_id, seats_booked) VALUES (?, ?, ?)";
        String updateBusSql = "UPDATE buses SET available_seats = available_seats - ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // 1️⃣ Check available seats
            int availableSeats = 0;
            try (PreparedStatement ps = conn.prepareStatement(checkSeatsSql)) {
                ps.setInt(1, booking.getBusId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    availableSeats = rs.getInt("available_seats");
                    if (availableSeats < booking.getSeatsBooked()) {
                        conn.rollback();
                        return false; // Not enough seats
                    }
                } else {
                    conn.rollback();
                    return false; // Bus not found
                }
            }

            // 2️⃣ Insert booking
            try (PreparedStatement ps = conn.prepareStatement(insertBookingSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, booking.getBusId());
                ps.setInt(2, booking.getUserId());
                ps.setInt(3, booking.getSeatsBooked());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) booking.setId(rs.getInt(1));
            }

            // 3️⃣ Update bus available seats
            try (PreparedStatement ps = conn.prepareStatement(updateBusSql)) {
                ps.setInt(1, booking.getSeatsBooked());
                ps.setInt(2, booking.getBusId());
                ps.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Alias for consistency
    public boolean createBooking(Booking booking) {
        return addBooking(booking);
    }

    // -------------------- GET ALL BOOKINGS --------------------
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT id, bus_id, user_id, seats_booked, booking_date FROM bookings ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setBusId(rs.getInt("bus_id"));
                b.setUserId(rs.getInt("user_id"));
                b.setSeatsBooked(rs.getInt("seats_booked"));
                b.setBookingDate(rs.getTimestamp("booking_date"));
                bookings.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }

    // -------------------- GET BOOKINGS BY BUS --------------------
    public List<Booking> getBookingsByBusId(int busId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT id, bus_id, user_id, seats_booked, booking_date FROM bookings WHERE bus_id = ? ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, busId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setBusId(rs.getInt("bus_id"));
                b.setUserId(rs.getInt("user_id"));
                b.setSeatsBooked(rs.getInt("seats_booked"));
                b.setBookingDate(rs.getTimestamp("booking_date"));
                bookings.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }

    // -------------------- GET BOOKINGS BY USER --------------------
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT id, bus_id, user_id, seats_booked, booking_date FROM bookings WHERE user_id = ? ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setBusId(rs.getInt("bus_id"));
                b.setUserId(rs.getInt("user_id"));
                b.setSeatsBooked(rs.getInt("seats_booked"));
                b.setBookingDate(rs.getTimestamp("booking_date"));
                bookings.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }
}