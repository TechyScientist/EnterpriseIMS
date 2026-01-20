<%@ page import="com.johnnyconsole.enterpriseims.persistence.User" %>
<% if(session.getAttribute("SignedInUser") == null) response.sendRedirect("/ims?error=401 (Unauthorized)&message=You must be signed in to do that.");
   else if(!((User)(session.getAttribute("SignedInUser"))).isAdministrator()) response.sendRedirect("/ims/dashboard.jsp?error=401 (Unauthorized)&message=You must be an administrator to do that.");%>
<html>
    <head>
        <title>IMS Web: Dashboard</title>
        <link rel="stylesheet" href="assets/style/main.css"/>
        <style>
            div#horizontal {
                display: grid;
                max-width: fit-content;
                margin-left: 30px;
                grid-template-columns: 1fr 1fr;
                gap: 30px;
            }

            div div a {
                margin-left: 30px;
            }
        </style>
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
            <h2>Administrative Functions</h2>
            <div id="horizontal">
                <div>
                    <h3>Asset Management</h3>
                    <a href="">Add an Asset</a>
                    <a href="">Modify an Existing Asset</a>
                    <a href="">Delete an Existing Asset</a>
                </div>
                <div>
                    <h3>User Management</h3>
                    <a href="">Provision a User Account</a>
                    <a href="">Modify an Existing User Account</a>
                    <a href="">Delete an Existing User Account</a>
                </div>
            </div>
        </main>
    </body>
</html>