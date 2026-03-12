<nav>
    <a href="dashboard.jsp"<% if(pageCategory.equals("dashboard")) { %> id="current" <% } %>>Dashboard</a>
    <a href="search.jsp"<% if(pageCategory.equals("search")) { %> id="current" <% } %>>Search</a>
    <a href="transactions.jsp"<% if(pageCategory.equals("transactions")) { %> id="current" <% } %>>Transactions</a>
    <% if(signedInUser.isAdministrator()) { %>
    <a href="admin.jsp"<% if(pageCategory.equals("administration")) { %> id="current" <% } %>>Administration</a>
    <% } %>
    <a href="SignOutServlet">Sign Out</a>
</nav>