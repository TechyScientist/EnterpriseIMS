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

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {

    @EJB
    UserDao userDao;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("SignedInUser") != null) {
            if (((User) (request.getSession().getAttribute("SignedInUser"))).isAdministrator()) {
                if(request.getParameter("delete-user-submit") != null) {
                    String username = request.getParameter("username").toLowerCase(),
                            adminUsername = request.getParameter("admin-username");

                    if(userDao.deleteUser(userDao.getUser(username), adminUsername)) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.sendRedirect("/ims/delete-user.jsp?user=deleted");
                    } else {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.sendRedirect("/ims/delete-user.jsp?error=500 (Internal Server Error)&message=This action could not be completed.");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.sendRedirect("/ims/delete-user.jsp?error=401 (Unauthorized)&message=This action must be initiated by the delete user form.");
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
