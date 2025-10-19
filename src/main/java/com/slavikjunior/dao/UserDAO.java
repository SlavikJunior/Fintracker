package com.slavikjunior.dao;

import java.sql.*;

public class UserDAO {

    private Connection conn = getConnection();

    private Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
        }
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/demo",
                    "postgres",
                    "7913"
            );
            System.out.println("Connected to PostgreSQL database successfully");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean userExists(String username) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM users WHERE username=?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public void createUser(String username, String password, String email) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)");
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, email);
        ps.executeUpdate();
    }

    public boolean checkCredentials(String username, String password) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
