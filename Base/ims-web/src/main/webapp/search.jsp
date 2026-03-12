<% String pageCategory = "search", pageTitle = "Search"; %>
<%@ include file="assets/include/header.jsp" %>

<% String error = request.getParameter("error"), message = request.getParameter("message");
    if(error != null) { %>
        <p id="error"><strong>Error <%= error %></strong>: <%= message %></p>
<% } %>
<h2>Perform an IMS Search</h2>

<%@ include file="assets/include/footer.jsp" %>