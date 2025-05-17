package com.dogresponse.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/updateList")
public class UpdateListServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int listId = Integer.parseInt(request.getParameter("listId"));
		String listName = request.getParameter("listName");
		String codesParam = request.getParameter("codes");

		if (codesParam == null || codesParam.trim().isEmpty()) {
			response.sendRedirect("pages/editList.jsp?listId=" + listId + "&error=NoCodesProvided");
			return;
		}

		String[] codes = codesParam.split(",");

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userId") == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		int userId = (int) session.getAttribute("userId");

		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dogresponse_db", "root",
				"root")) {

			try (PreparedStatement checkStmt = con
					.prepareStatement("SELECT COUNT(*) FROM dog_lists WHERE id=? AND user_id=?")) {
				checkStmt.setInt(1, listId);
				checkStmt.setInt(2, userId);
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next() && rs.getInt(1) == 0) {
						throw new ServletException("List ID " + listId + " does not exist or does not belong to user.");
					}
				}
			}

			try (PreparedStatement ps = con.prepareStatement("UPDATE dog_lists SET name=? WHERE id=? AND user_id=?")) {
				ps.setString(1, listName);
				ps.setInt(2, listId);
				ps.setInt(3, userId);
				ps.executeUpdate();
			}

			try (PreparedStatement ps = con.prepareStatement("DELETE FROM list_codes WHERE list_id=?")) {
				ps.setInt(1, listId);
				ps.executeUpdate();
			}

			try (PreparedStatement ps = con.prepareStatement("INSERT INTO list_codes(list_id, code) VALUES(?,?)")) {
				for (String code : codes) {
					if (!code.trim().isEmpty()) {
						ps.setInt(1, listId);
						ps.setString(2, code.trim());
						ps.addBatch();
					}
				}
				ps.executeBatch();
			}

			response.sendRedirect("pages/lists.jsp");

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("pages/editList.jsp?listId=" + listId + "&error=UpdateFailed");
		}
	}
}
