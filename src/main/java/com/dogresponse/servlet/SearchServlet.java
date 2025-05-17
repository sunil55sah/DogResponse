package com.dogresponse.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
	private static final String[] ALL_CODES = { "100", "101", "102", "103", "200", "201", "202", "203", "204", "205",
			"206", "207", "208", "226", "300", "301", "302", "303", "304", "305", "307", "308", "400", "401", "402",
			"403", "404", "405", "406", "407", "408", "409", "410", "411", "412", "413", "414", "415", "416", "417",
			"418", "421", "422", "423", "424", "425", "426", "428", "429", "431", "451", "500", "501", "502", "503",
			"504", "505", "506", "507", "508", "510", "511" };

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String filter = request.getParameter("filter").toLowerCase().trim();
		List<String> matchedCodes = new ArrayList<>();

		for (String code : ALL_CODES) {
			if (matchesFilter(code, filter)) {
				matchedCodes.add(code);
			}
		}

		request.setAttribute("codes", matchedCodes);
		request.setAttribute("filter", filter);
		RequestDispatcher rd = request.getRequestDispatcher("search.jsp");
		rd.forward(request, response);
	}

	private boolean matchesFilter(String code, String filter) {
		if (filter.contains("x")) {
			for (int i = 0; i < filter.length(); i++) {
				char c = filter.charAt(i);
				if (c != 'x' && c != code.charAt(i))
					return false;
			}
			return true;
		} else {
			return code.equals(filter);
		}
	}
}
