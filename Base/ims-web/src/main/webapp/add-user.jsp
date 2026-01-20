<%@ page import="com.johnnyconsole.enterpriseims.persistence.User" %>
<% if(session.getAttribute("SignedInUser") == null) response.sendRedirect("/ims?error=401 (Unauthorized)&message=You must be signed in to do that.");
   else if(!((User)(session.getAttribute("SignedInUser"))).isAdministrator()) response.sendRedirect("/ims/dashboard.jsp?error=401 (Unauthorized)&message=You must be an administrator to do that.");%>
<html>
    <head>
        <title>IMS Web: Administration</title>
        <link rel="stylesheet" href="assets/style/main.css"/>
    </head>
    <body>
        <header>
            <h1>IMS Web: Administration</h1>
        </header>
        <nav>
            <a href="dashboard.jsp">Dashboard</a>
            <a href="search.jsp">Search</a>
            <a href="transactions.jsp">Transactions</a>
            <a href="admin.jsp" id="current">Administration</a>
            <a href="SignOutServlet">Sign Out</a>
        </nav>
        <main>
            <% if(request.getParameter("user") != null) { %>
                <p id="success"><strong>The user account has been added.</strong></p>
            <% } %>
            <% String error = request.getParameter("error"), message = request.getParameter("message");
                if(error != null) { %>
                <p id="error"><strong>Error <%= error %></strong>: <%= message %></p>
                <% } %>
            <h2>Provision a User Account</h2>
            <form action="AddUserServlet" method="post">
                <label for="username">Username:</label>
                <input type="text" name="username" id="username" required/><br/><br/>
                <label for="name">Name:</label>
                <input type="text" name="name" id="name" required/><br/><br/>
                <label for="password">Password: </label>
                <input type="password" name="password" id="password" required/><br/><br/>
                <label for="confirm-password">Confirm Password:</label>
                <input type="password" name="confirm-password" id="confirm-password" required/><br/><br/>
                <label for="administrator">Is this user an administrator?</label>
                <select name="administrator" id="administrator">
                    <option value="false" selected>No</option>
                    <option value="true">Yes</option>
                </select><br/><br/>
                <input type="submit" name="add-user-submit" id="add-user-submit" value="Submit"/>
            </form>
        </main>
    </body>
</html>