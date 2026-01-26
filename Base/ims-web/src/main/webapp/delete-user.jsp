<%@ page import="com.johnnyconsole.enterpriseims.persistence.User" %>
<%@ page import="com.johnnyconsole.enterpriseims.persistence.interfaces.UserDao" %>
<%@ page import="java.util.List" %>
<% if(session.getAttribute("SignedInUser") == null || session.getAttribute("UserDao") == null) response.sendRedirect("/ims?error=401 (Unauthorized)&message=You must be signed in to do that.");
   else if(!((User)(session.getAttribute("SignedInUser"))).isAdministrator()) response.sendRedirect("/ims/dashboard.jsp?error=401 (Unauthorized)&message=You must be an administrator to do that.");
   UserDao userDao = (UserDao)session.getAttribute("UserDao");
   User signedIn = (User)(session.getAttribute("SignedInUser"));
   List<User> users = userDao.getUsersExcept(signedIn.getUsername());
%>
<html>
    <head>
        <title>IMS Web: Administration</title>
        <link rel="stylesheet" href="assets/style/main.css"/>
        <link rel="icon" href="assets/img/icon.png"/>
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
                <p id="success"><strong>The user account has been deleted.</strong></p>
            <% } %>
            <% String error = request.getParameter("error"), message = request.getParameter("message");
                if(error != null) { %>
                <p id="error"><strong>Error <%= error %></strong>: <%= message %></p>
                <% } %>
            <h2>Delete a User Account</h2>
            <form action="DeleteUserServlet" method="post">
                <input type="hidden" name="admin-username" id="admin-username" value="<%= signedIn.getUsername() %>"/>
                <label for="username">Username:</label>
                <select name="username" id="username">
                    <%
                        if(users.isEmpty()) {%>
                            <option value="" disabled>No Users Found</option>
                        <% } else {
                            for(User user: users) { %>
                                <option value="<%= user.getUsername() %>"><%= user.getName() %> (<%= user.getUsername()%>)</option>
                        <%  }
                        }%>
                </select><br/><br/>
                <input type="submit" name="delete-user-submit" id="delete-user-submit" value="Submit"/>
            </form>
        </main>
    </body>
</html>