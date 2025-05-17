<%@ page import="java.sql.*, java.util.*, jakarta.servlet.http.*, jakarta.servlet.*" %>
<%@ page session="true" %>
<%
    HttpSession sessionObj = request.getSession(false);
    if (sessionObj == null || sessionObj.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    int userId = (int) sessionObj.getAttribute("userId");
    Connection conn = null;
    PreparedStatement psLists = null;
    PreparedStatement psItems = null;
    ResultSet rsLists = null;
    ResultSet rsItems = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dogresponse_db", "root", "root");

      
        String sqlLists = "SELECT * FROM saved_lists WHERE user_id = ?";
        psLists = conn.prepareStatement(sqlLists);
        psLists.setInt(1, userId);
        rsLists = psLists.executeQuery();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Your Saved Lists</title>
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
</head>
<body class="container mt-5">
    <h2 class="mb-4">Your Saved Dog Lists</h2>

<%
    while (rsLists.next()) {
        int listId = rsLists.getInt("id");
        String listName = rsLists.getString("name"); 
        String date = rsLists.getString("created_at");
        String filterUsed = rsLists.getString("filter_used");

        // Fetch list items for this list
        String sqlItems = "SELECT response_code, image_url FROM list_items WHERE list_id = ?";
        psItems = conn.prepareStatement(sqlItems);
        psItems.setInt(1, listId);
        rsItems = psItems.executeQuery();
%>
    <div class="card mb-4 shadow-sm">
        <div class="card-header d-flex justify-content-between align-items-center">
            <div>
                <h5 class="mb-0"><%= listName %></h5>
                <small class="text-muted">Created on: <%= date %> | Filter Used: <%= filterUsed %></small>
            </div>
            <div>
                <form method="post" action="deleteList" style="display:inline;">
                    <input type="hidden" name="listId" value="<%= listId %>" />
                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Delete this list?')">Delete</button>
                </form>
                <form method="get" action="editList.jsp" style="display:inline;">
                    <input type="hidden" name="listId" value="<%= listId %>" />
                    <button type="submit" class="btn btn-warning btn-sm">Edit</button>
                </form>
            </div>
        </div>
        <div class="card-body">
            <div class="d-flex flex-wrap">
<%
        while (rsItems.next()) {
            String code = rsItems.getString("response_code");
            String imageUrl = rsItems.getString("image_url");
%>
                <div class="text-center m-2">
                    <img src="<%= imageUrl %>" alt="Code <%= code %>" class="rounded shadow-sm" style="height: 150px;" />
                    <p class="mt-2 mb-0"><strong><%= code %></strong></p>
                </div>
<%
        }
        rsItems.close();
        psItems.close();
%>
            </div>
        </div>
    </div>
<%
    } 
%>
</body>
</html>
<%
    } catch (Exception e) {
        out.println("<div class='alert alert-danger'>Error: " + e.getMessage() + "</div>");
    } finally {
        try { if (rsLists != null) rsLists.close(); } catch (Exception e) {}
        try { if (psLists != null) psLists.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
%>
