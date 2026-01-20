<%@ page import="com.johnnyconsole.enterpriseims.persistence.User" %>
<% if(session.getAttribute("SignedInUser") == null) response.sendRedirect("/ims?error=401 (Unauthorized)&message=You must be signed in to do that.");
   else if(!((User)(session.getAttribute("SignedInUser"))).isAdministrator()) response.sendRedirect("/ims/dashboard.jsp?error=401 (Unauthorized)&message=You must be an administrator to do that.");%>
<html>
    <head>
        <title>IMS Web: Dashboard</title>
        <link rel="stylesheet" href="assets/style/main.css"/>
    </head>
    <body>
        <header>
            <h1>IMS Web: Administration</h1>
        </header>
        <nav>
            <a href="dashboard.jsp">Dashboard</a>
            <a href="search.jsp">Search</a>
            <a href="admin.jsp" id="current">Administration</a>
            <a href="SignOutServlet">Sign Out</a>
        </nav>
        <main>
            <h2>Administrative Functions</h2>
        </main>
    </body>
</html>