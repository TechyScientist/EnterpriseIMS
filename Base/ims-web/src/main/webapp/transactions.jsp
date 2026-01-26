<%@ page import="com.johnnyconsole.enterpriseims.persistence.User" %>
<% if(session.getAttribute("SignedInUser") == null) response.sendRedirect("/ims?error=401 (Unauthorized)&message=You must be signed in to do that."); %>
<html>
    <head>
        <title>IMS Web: Transactions</title>
        <link rel="stylesheet" href="assets/style/main.css"/>
        <link rel="icon" href="assets/img/icon.png"/>
    </head>
    <body>
        <% User user = (User)(session.getAttribute("SignedInUser")); %>
        <header>
            <h1>IMS Web: Transactions</h1>
        </header>
        <nav>
            <a href="dashboard.jsp">Dashboard</a>
            <a href="search.jsp">Search</a>
            <a href="transactions.jsp" id="current">Transactions</a>
            <% if(user.isAdministrator()) { %>
                <a href="admin.jsp">Administration</a>
            <% } %>
            <a href="SignOutServlet">Sign Out</a>
        </nav>
        <main>
            <% String error = request.getParameter("error"), message = request.getParameter("message");
                if(error != null) { %>
                    <p id="error"><strong>Error <%= error %></strong>: <%= message %></p>
            <% } %>
            <h2>Execute an IMS Transaction</h2>
        </main>
    </body>
</html>