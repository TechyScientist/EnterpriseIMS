<%
    if(session.getAttribute("SignedInUser") != null) response.sendRedirect("dashboard.jsp");
%>

<html>
    <head>
        <title>IMS Web - Sign In</title>
        <link rel="stylesheet" type="text/css" href="assets/style/main.css">
    </head>
    <body>
        <header>
            <h1>IMS Web - Sign In</h1>
        </header>
        <main>
            <p id="warning"><strong>Warning</strong>: Access to IMS Web is restricted to authorized users only. Please sign in to continue.</p>
            <h2>Sign in to IMS Web</h2>
            <form action="SignInServlet" method="post">
                <label for="username">IMS Username:</label>
                <input type="text" name="username" id="username" required/><br/><br/>
                <label for="password">IMS Password:</label>
                <input type="password" name="password" id="password" required/><br/><br/>
                <input type="submit" name="ims-signin-submit" value="Sign In"/>
            </form>
        </main>
    </body>
</html>
