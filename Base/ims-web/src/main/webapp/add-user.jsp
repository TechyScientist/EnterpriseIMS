<% String pageCategory = "administration", pageTitle = "Add a User"; %>
<%@ include file="assets/include/header-admin.jsp" %>

<% if(request.getParameter("user") != null) { %>
    <p id="success"><strong>The user account has been added.</strong></p>
<% } %>
<% String error = request.getParameter("error"), message = request.getParameter("message");
    if(error != null) { %>
    <p id="error"><strong>Error <%= error %></strong>: <%= message %></p>
    <% } %>
<h2>Provision a User Account</h2>
<form action="AddUserServlet" method="post">
    <label for="username">Username:</label>
    <input type="text" name="username" id="username" required/><br/><br/>
    <label for="name">Name:</label>
    <input type="text" name="name" id="name" required/><br/><br/>
    <label for="password">Password: </label>
    <input type="password" name="password" id="password" required/><br/><br/>
    <label for="confirm-password">Confirm Password:</label>
    <input type="password" name="confirm-password" id="confirm-password" required/><br/><br/>
    <label for="administrator">Is this user an administrator?</label>
    <select name="administrator" id="administrator">
        <option value="false" selected>No</option>
        <option value="true">Yes</option>
    </select><br/><br/>
    <input type="submit" name="add-user-submit" id="add-user-submit" value="Submit"/>
</form>

<%@ include file="assets/include/footer.jsp" %>