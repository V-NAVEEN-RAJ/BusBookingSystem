package com.naveenraj.bus_booking_system.controller;

import com.naveenraj.bus_booking_system.dao.UserDao;
import com.naveenraj.bus_booking_system.model.User;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/user")
public class UserController extends HttpServlet {

    private UserDao userDao = new UserDao();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String body = sb.toString();

        // Parse incoming JSON
        User requestUser = gson.fromJson(body, User.class);

        try {
            if (requestUser.getName() != null && !requestUser.getName().isEmpty()) {
                // -------------------- SIGNUP --------------------
                // role defaults to USER if not provided
                if (requestUser.getRole() == null) requestUser.setRole("USER");
                boolean created = userDao.addUser(requestUser);
                if (created) {
                    resp.getWriter().write(gson.toJson(new ResponseMessage("Signup successful!")));
                } else {
                    resp.getWriter().write(gson.toJson(new ResponseMessage(
                            "Failed to create user. Email might already exist."
                    )));
                }
            } else {
                // -------------------- LOGIN --------------------
                User user = userDao.login(requestUser.getEmail(), requestUser.getPassword());
                if (user != null) {
                    resp.getWriter().write(gson.toJson(user));
                } else {
                    resp.getWriter().write(gson.toJson(new ResponseMessage("Invalid email or password!")));
                }
            }
        } catch (Exception e) {
            resp.getWriter().write(gson.toJson(new ResponseMessage("Error: " + e.getMessage())));
        }
    }

    // -------------------- GET (List users or get by ID) --------------------
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        String idParam = req.getParameter("id");
        String emailParam = req.getParameter("email");

        try {
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                User user = userDao.getUserById(id);
                resp.getWriter().write(gson.toJson(user));
            } else if (emailParam != null) {
                User user = userDao.getUserByEmail(emailParam);
                resp.getWriter().write(gson.toJson(user));
            } else {
                resp.getWriter().write(gson.toJson(userDao.getAllUsers()));
            }
        } catch (Exception e) {
            resp.getWriter().write(gson.toJson(new ResponseMessage("Error: " + e.getMessage())));
        }
    }

    // -------------------- DELETE --------------------
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.getWriter().write(gson.toJson(new ResponseMessage("Missing user ID")));
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            boolean deleted = userDao.deleteUser(id);
            if (deleted) {
                resp.getWriter().write(gson.toJson(new ResponseMessage("User deleted successfully")));
            } else {
                resp.getWriter().write(gson.toJson(new ResponseMessage("Failed to delete user")));
            }
        } catch (Exception e) {
            resp.getWriter().write(gson.toJson(new ResponseMessage("Error: " + e.getMessage())));
        }
    }

    // -------------------- RESPONSE MESSAGE CLASS --------------------
    private static class ResponseMessage {
        private String message;
        public ResponseMessage(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}