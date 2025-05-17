package com.dogresponse.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/saveList")
public class SaveListServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userId") == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		int userId = (int) session.getAttribute("userId");

		String listName = request.getParameter("listName");
		String filterUsed = request.getParameter("filterUsed");
		String[] codes = request.getParameterValues("codes[]");

		if (listName == null || codes == null) {
			response.sendRedirect("search.jsp");
			return;
		}

		Connection conn = null;
		PreparedStatement psList = null;
		PreparedStatement psItem = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dogresponse_db", "root", "root");

			String insertListSql = "INSERT INTO saved_lists (user_id, name, filter_used) VALUES (?, ?, ?)";
			psList = conn.prepareStatement(insertListSql, Statement.RETURN_GENERATED_KEYS);
			psList.setInt(1, userId);
			psList.setString(2, listName);
			psList.setString(3, filterUsed);

			psList.executeUpdate();

			rs = psList.getGeneratedKeys();
			int listId = 0;
			if (rs.next()) {
				listId = rs.getInt(1);
			}

			String insertItemSql = "INSERT INTO list_items (list_id, response_code, image_url) VALUES (?, ?, ?)";
			psItem = conn.prepareStatement(insertItemSql);

			for (String code : codes) {
				String imageUrl = "https://http.dog/" + code + ".jpg";
				psItem.setInt(1, listId);
				psItem.setString(2, code);
				psItem.setString(3, imageUrl);
				psItem.addBatch();
			}
			psItem.executeBatch();

			response.sendRedirect("lists.jsp");

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().println("Error saving list: " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (psList != null)
					psList.close();
			} catch (Exception e) {
			}
			try {
				if (psItem != null)
					psItem.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
	}
}
