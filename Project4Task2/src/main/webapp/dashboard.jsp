<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Service Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        table { width: 90%; border-collapse: collapse; margin-top: 20px; }
        table, th, td { border: 1px solid #ccc; }
        th, td { padding: 12px; text-align: left; }
        h1 { margin-bottom: 20px; }
    </style>
</head>
<body>

<h1>Service Dashboard</h1>

<h2>Statistics</h2>
<table>
    <tr>
        <th>Most Frequent Country</th>
        <td><%= request.getAttribute("popularCountry") %></td>
    </tr>
    <tr>
        <th>Total Requests</th>
        <td><%= request.getAttribute("totalRequests") %></td>
    </tr>
    <tr>
        <th>Average # of Universities Returned</th>
        <td><%= request.getAttribute("avgResults") %></td>
    </tr>
</table>

<h2>Logged Requests</h2>

<table>
    <tr>
        <th>Country</th>
        <th>Result Count</th>
        <th>Time</th>
    </tr>

    <%
        List<String> logs = (List<String>) request.getAttribute("rawLogs");
        if (logs != null) {
            for (String item : logs) {
                String[] parts = item.split("\\|");
    %>
    <tr>
        <td><%= parts[0].trim() %></td>
        <td><%= parts[1].trim() %></td>
        <td><%= parts[2].trim() %></td>
    </tr>
    <%
            }
        }
    %>
</table>

<br>
<a href="index.jsp">Back</a>

</body>
</html>
