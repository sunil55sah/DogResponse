package com.dogresponse.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/dogresponse_db";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "root";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement checkStmt = null;
		ResultSet rs = null;

		try {
			// Load MySQL driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Connect to database
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

			// Check if email already exists
			checkStmt = conn.prepareStatement("SELECT id FROM users WHERE email = ?");
			checkStmt.setString(1, email);
			rs = checkStmt.executeQuery();

			if (rs.next()) {
				// Email already exists
				response.getWriter().println("Error: Email is already registered. Please login instead.");
			} else {
				// Insert new user
				stmt = conn.prepareStatement("INSERT INTO users(email, password) VALUES (?, ?)");
				stmt.setString(1, email);
				stmt.setString(2, password);

				int rows = stmt.executeUpdate();

				if (rows > 0) {
					response.sendRedirect("login.jsp");
				} else {
					response.getWriter().println("Signup failed. Try again.");
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			response.getWriter().println("Error: " + e.getMessage());
		} finally {
			// Close resources
			try {
				if (rs != null)
					rs.close();
				if (checkStmt != null)
					checkStmt.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
