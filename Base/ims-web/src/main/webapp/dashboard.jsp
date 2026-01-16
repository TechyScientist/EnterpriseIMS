<%@ page import="com.johnnyconsole.enterpriseims.persistence.User" %>
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
            <% User user = (User)(session.getAttribute("SignedInUser"));
                String name = user.getName();
                if(name.contains(" ")) name = name.substring(0, name.indexOf(' '));
            %>
            <h3>Welcome, <%= name %></h3>
        </main>
    </body>
</html>