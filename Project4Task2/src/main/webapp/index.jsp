<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>University Lookup</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        label { font-weight: bold; }
        input[type=text] { padding: 6px; width: 250px; }
        input[type=submit] { padding: 6px 14px; margin-top: 10px; }
        .msg { color: #d00; margin-top: 10px; }
    </style>
</head>

<body>

<h1>University Search</h1>
<p>Enter a country name to explore universities.</p>

<!--
 Using relative paths here ensures the servlet is reached correctly
 regardless of the final deployed context path.
-->
<form action="GetUniversity" method="GET">
    <label>Country:</label>
    <input type="text" name="country" />
    <br>
    <input type="submit" value="Search" />
</form>

<!-- Link to dashboard (same servlet, different route) -->
<form action="getDashBoard" method="GET" style="margin-top: 20px;">
    <label>View Dashboard</label>
    <br>
    <input type="submit" value="Open Dashboard" />
</form>

<!-- Display error message if servlet set one -->
<div class="msg">
    <%= (request.getAttribute("error") != null)
            ? request.getAttribute("error")
            : "" %>
</div>

</body>
</html>