<%@ page import="java.util.List" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Search Dog Response Codes</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h2>Search Dog HTTP Codes</h2>
    <form method="post" action="search">
        <label>Enter Filter (e.g., 200, 2xx, 20x):</label>
        <input type="text" name="filter" required />
        <button type="submit">Search</button>
    </form>

    <%
        List<String> codes = (List<String>) request.getAttribute("codes");
        String filter = (String) request.getAttribute("filter");
        if (codes != null && !codes.isEmpty()) {
    %>
    <h3>Results for "<%= filter %>"</h3>
    <form method="post" action="saveList">
        <label>List Name:</label>
        <input type="text" name="listName" required />
        <input type="hidden" name="filterUsed" value="<%= filter %>" />

        <div style="display: flex; flex-wrap: wrap; gap: 10px;">
            <% for (String code : codes) { %>
                <div style="text-align: center;">
                    <img src="https://http.dog/<%= code %>.jpg" width="150" />
                    <p><%= code %></p>
                    <input type="hidden" name="codes[]" value="<%= code %>" />
                </div>
            <% } %>
        </div>

        <button type="submit">Save List</button>
    </form>
    <% } else if (filter != null) { %>
        <p>No results found for "<%= filter %>"</p>
    <% } %>
</body>
</html>
