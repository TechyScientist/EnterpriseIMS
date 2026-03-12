<%@ page import="com.johnnyconsole.enterpriseims.persistence.User" %>
<% User signedInUser = (User) session.getAttribute("SignedInUser");
if(signedInUser == null) response.sendRedirect("/ims?error=401 (Unauthorized)&message=You must be signed in to do that.");
else {%>
    <html>
    <head>
        <title>IMS Web<% if(pageTitle != null) { %>: <%= pageTitle %><% } %></title>
        <link rel="stylesheet" href="assets/style/main.css"/>
        <link rel="icon" href="assets/img/icon.png"/>
    </head>
    <body>
    <header>
        <h1>IMS Web<% if(pageTitle != null) { %>: <%= pageTitle %><% } %></h1>
    </header>
    <%@ include file="nav.jsp" %>
    <main>