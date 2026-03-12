<% String pageCategory = "administration", pageTitle = "Administration"; %>
<%@ include file="assets/include/header-admin.jsp" %>
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

<h2>Administrative Functions</h2>
<div id="horizontal">
    <div>
        <h3>Asset Management</h3>
        <a href="add-asset.jsp">Add an Asset</a>
        <a href="modify-asset.jsp">Modify an Existing Asset</a>
        <a href="delete-asset.jsp">Delete an Existing Asset</a>
    </div>
    <div>
        <h3>User Management</h3>
        <a href="add-user.jsp">Provision a User Account</a>
        <a href="modify-user.jsp">Modify an Existing User Account</a>
        <a href="delete-user.jsp">Delete an Existing User Account</a>
    </div>
</div>

<%@ include file="assets/include/footer.jsp" %>