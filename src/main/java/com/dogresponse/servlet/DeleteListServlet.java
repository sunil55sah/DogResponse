package com.dogresponse.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/deleteList")
public class DeleteListServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int listId = Integer.parseInt(request.getParameter("listId"));

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dogresponse_db", "root",
				"root")) {
			Class.forName("com.mysql.cj.jdbc.Driver");

			try (PreparedStatement psItems = conn.prepareStatement("DELETE FROM list_items WHERE list_id = ?")) {
				psItems.setInt(1, listId);
				psItems.executeUpdate();
			}

			try (PreparedStatement psList = conn.prepareStatement("DELETE FROM saved_lists WHERE id = ?")) {
				psList.setInt(1, listId);
				psList.executeUpdate();
			}

			response.sendRedirect("lists.jsp");

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().println("Error deleting list: " + e.getMessage());
		}
	}
}
