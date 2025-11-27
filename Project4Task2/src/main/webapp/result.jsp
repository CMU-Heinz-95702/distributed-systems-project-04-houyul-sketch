<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="org.json.simple.JSONArray" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>University Result</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        table { width: 60%; border-collapse: collapse; margin-top: 20px; }
        table, th, td { border: 1px solid #ccc; }
        th, td { padding: 10px; text-align: left; }
    </style>
</head>
<body>

<h1>University Information</h1>

<%
    JSONObject obj = (JSONObject) request.getAttribute("result");
    JSONArray domains = (JSONArray) obj.get("domains");
    JSONArray pages = (JSONArray) obj.get("web_pages");
%>

<table>
    <tr><th>Name</th><td><%= obj.get("name") %></td></tr>
    <tr><th>Country</th><td><%= obj.get("country") %></td></tr>
    <tr><th>State / Province</th><td><%= obj.get("state-province") != null ? obj.get("state-province") : "N/A" %></td></tr>
    <tr><th>Domain</th><td><%= domains.get(0) %></td></tr>
    <tr><th>Website</th><td><a href="<%= pages.get(0) %>"><%= pages.get(0) %></a></td></tr>
</table>

<br>
<a href="index.jsp">Back</a>

</body>
</html>
