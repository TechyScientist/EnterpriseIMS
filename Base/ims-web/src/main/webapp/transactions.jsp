<% String pageCategory = "transactions", pageTitle = "Transactions"; %>
<%@ include file="assets/include/header.jsp" %>

<% String error = request.getParameter("error"), message = request.getParameter("message");
    if(error != null) { %>
        <p id="error"><strong>Error <%= error %></strong>: <%= message %></p>
<% } %>
<h2>Execute an IMS Transaction</h2>

<%@ include file="assets/include/footer.jsp" %>