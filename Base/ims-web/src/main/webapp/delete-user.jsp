<% String pageName = "administration", pageTitle = "Delete a User"; %>
<%@ include file="assets/include/header-admin.jsp" %>
<%@ page import="com.johnnyconsole.enterpriseims.persistence.interfaces.UserDao" %>
<%@ page import="java.util.List" %>

<% UserDao userDao = (UserDao)session.getAttribute("UserDao");
   List<User> users = userDao.getUsersExcept(signedInUser.getUsername());
 if(request.getParameter("user") != null) { %>
        <p id="success"><strong>The user account has been deleted.</strong></p>
<% }
 String error = request.getParameter("error"), message = request.getParameter("message");
 if(error != null) { %>
    <p id="error"><strong>Error <%= error %></strong>: <%= message %></p>
<% } %>

<h2>Delete a User Account</h2>
<form action="DeleteUserServlet" method="post">
    <input type="hidden" name="admin-username" id="admin-username" value="<%= signedInUser.getUsername() %>"/>
    <label for="username">Username:</label>
    <select name="username" id="username">
        <% if(users.isEmpty()) {%>
                <option value="" disabled>No Users Found</option>
            <% } else {
                for(User user: users) { %>
                    <option value="<%= user.getUsername() %>"><%= user.getName() %> (<%= user.getUsername()%>)</option>
            <%  }
            }%>
    </select><br/><br/>
    <input type="submit" name="delete-user-submit" id="delete-user-submit" value="Submit"/>
</form>

<%@ include file="assets/include/footer.jsp" %>