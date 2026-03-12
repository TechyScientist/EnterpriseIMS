<nav>
    <a href="dashboard.jsp"<% if(pageName.equals("dashboard")) { %> id="current" <% } %>>Dashboard</a>
    <a href="search.jsp"<% if(pageName.equals("search")) { %> id="current" <% } %>>Search</a>
    <a href="transactions.jsp"<% if(pageName.equals("transactions")) { %> id="current" <% } %>>Transactions</a>
    <% if(signedInUser.isAdministrator()) { %>
    <a href="admin.jsp"<% if(pageName.equals("administration")) { %> id="current" <% } %>>Administration</a>
    <% } %>
    <a href="SignOutServlet">Sign Out</a>
</nav>