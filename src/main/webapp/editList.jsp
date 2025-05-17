<%@ page import="java.sql.*, java.util.*" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
  

    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    int userId = (int) session.getAttribute("userId");
    String listIdStr = request.getParameter("listId");

    if (listIdStr == null) {
        response.sendRedirect("lists.jsp");
        return;
    }

    int listId = Integer.parseInt(listIdStr);
    String listName = "";
    List<String> codes = new ArrayList<>();

    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dogresponse_db", "root", "root");

        // Fetch list name
        PreparedStatement ps = con.prepareStatement("SELECT name FROM dog_lists WHERE id=? AND user_id=?");
        ps.setInt(1, listId);
        ps.setInt(2, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            listName = rs.getString("name");
        } else {
            response.sendRedirect("lists.jsp");
            return;
        }

        // Fetch codes
        ps = con.prepareStatement("SELECT code FROM list_codes WHERE list_id=?");
        ps.setInt(1, listId);
        rs = ps.executeQuery();
        while (rs.next()) {
            codes.add(rs.getString("code"));
        }

        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    String codeString = String.join(",", codes);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit List</title>
    <link rel="stylesheet" href="../css/style.css">
 
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-5">

    <h2 class="mb-4">Edit Dog List</h2>

    <form method="post" action="../updateList">
        <input type="hidden" name="listId" value="<%= listId %>" />

        <div class="mb-3">
            <label class="form-label">List Name:</label>
            <input type="text" class="form-control" name="listName" value="<%= listName %>" required />
        </div>

        <div class="mb-3">
            <label class="form-label">Response Codes (comma separated):</label>
            <input type="text" class="form-control" name="codes" value="<%= codeString %>" required />
        </div>

        <button type="submit" class="btn btn-primary">Update List</button>
        <a href="lists.jsp" class="btn btn-secondary ms-2">Back to Lists</a>
    </form>

</body>
</html>
