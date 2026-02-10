package com.naveenraj.bus_booking_system.controller;

import com.naveenraj.bus_booking_system.utils.DBUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/db-test")
public class DBTestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try (Connection con = DBUtil.getConnection()) {
            resp.getWriter().write("NeonDB Connection SUCCESS ✅");
        } catch (Exception e) {
            resp.getWriter().write("NeonDB Connection FAILED ❌");
        }
    }
}
