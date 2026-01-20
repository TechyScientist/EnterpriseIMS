package com.johnnyconsole.enterpriseims.servlet;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.johnnyconsole.enterpriseims.persistence.User;
import com.johnnyconsole.enterpriseims.persistence.interfaces.UserDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AddUserServlet")
public class AddUserServlet extends HttpServlet {

    @EJB
    UserDao userDao;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("SignedInUser") != null) {
            if (((User) (request.getSession().getAttribute("SignedInUser"))).isAdministrator()) {
                if(request.getParameter("add-user-submit") != null) {
                    String username = request.getParameter("username").toLowerCase(),
                            name = request.getParameter("name"),
                            password = request.getParameter("password"),
                            confirmPassword = request.getParameter("confirm-password");
                    boolean isAdmin = Boolean.parseBoolean(request.getParameter("administrator"));

                    if (!userDao.userExists(username)) {
                        if (password.equals(confirmPassword)) {
                            String hash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                            userDao.addUser(new User(username, name, hash, isAdmin));
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.sendRedirect("/ims/add-user.jsp?user=added");
                        } else {
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                            response.sendRedirect("/ims/add-user.jsp?error=409 (Conflict)&message=Your passwords do not match.");
                        }
                    } else {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        response.sendRedirect("/ims/add-user.jsp?error=409 (Conflict)&message=A user with the username '" + username + "' already exists.");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.sendRedirect("/ims/add-user.jsp?error=401 (Unauthorized)&message=This action must be initiated by the sign in form.");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendRedirect("/ims?error=401 (Unauthorized)&message=You must be an administrator to do that.");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/ims?error=401 (Unauthorized)&message=You need to be signed in to do that.");

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.sendRedirect("/ims/admin.jsp?error=405 (Method Not Allowed)&message=This action requires a POST request.");
    }
}
