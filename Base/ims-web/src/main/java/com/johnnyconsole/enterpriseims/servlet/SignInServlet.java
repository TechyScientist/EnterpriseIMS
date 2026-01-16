package com.johnnyconsole.enterpriseims.servlet;

import com.johnnyconsole.enterpriseims.persistence.User;
import com.johnnyconsole.enterpriseims.persistence.interfaces.UserDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/SignInServlet")
public class SignInServlet extends HttpServlet {

    @EJB
    UserDao userDao;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if(request.getParameter("ims-signin-submit") != null) {
                String username = request.getParameter("username").toLowerCase(),
                        password = request.getParameter("password");
                if (userDao.verifyUserPassword(username, password)) {
                    User user = userDao.getUser(username);

                    HttpSession session = request.getSession();
                    session.setAttribute("SignedInUser", user);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.sendRedirect("dashboard.jsp");
                }
                else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.sendRedirect("/ims?error=401 (Unauthorized)&message=Invalid credentials, please try again.");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendRedirect("/ims?error=401 (Unauthorized)&message=This action must be initiated by the sign in form.");
            }
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.sendRedirect("/temperature-suite?error=500 (Server Error)&message=" + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.sendRedirect("/ims?error=405 (Method Not Allowed)&message=This action requires a POST request.");
    }
}
