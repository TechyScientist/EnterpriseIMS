<%@ page import="com.johnnyconsole.enterpriseims.persistence.User" %>
<% if(session.getAttribute("SignedInUser") == null) response.sendRedirect("/ims?error=401 (Unauthorized)&message=You must be signed in to do that."); %>
<html>
    <head>
        <title>IMS Web - Dashboard</title>
        <link rel="stylesheet" href="assets/style/main.css"/>
    </head>
    <body>
        <header>
            <h2>IMS Web - Dashboard</h2>
        </header>
        <nav>
            <a href="SignOutServlet">Sign Out</a>
        </nav>
        <main>
            <% User user = (User)(session.getAttribute("SignedInUser")); %>
            <h3>Signed In as: <%= user.getUsername() %> <%
                if(user.isAdministrator()) { %>
                    <span style="display: inline-block; padding: 5px; font-size: 10px; font-weight: normal; vertical-align: middle; background: var(--color-primary); color: white; border-radius: 16px;">Adminstrator</span>
                <% } %>
            </h3>
        </main>
    </body>
</html>