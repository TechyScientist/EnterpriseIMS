<% String pageName = "dashboard", pageTitle = "Dashboard"; %>
<%@ include file="assets/include/header.jsp" %>

<% String error = request.getParameter("error"), message = request.getParameter("message");
    if(error != null) { %>
        <p id="error"><strong>Error <%= error %></strong>: <%= message %></p>
<% } %>
<h2>Signed In as: <%= signedInUser.getUsername() %> <%
    if(signedInUser.isAdministrator()) { %>
        <span style="display: inline-block; padding: 5px; font-size: 10px; font-weight: normal; vertical-align: middle; background: var(--color-primary); color: white; border-radius: 16px;">Adminstrator</span>
    <% } %>
</h2>

<%@ include file="assets/include/footer.jsp" %>