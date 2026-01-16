package com.johnnyconsole.enterpriseims.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SignOutServlet")
public class SignOutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
       try {
           request.getSession().invalidate();
           response.setStatus(200);
           response.sendRedirect("/ims?signout=success");
       } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.sendRedirect("/ims?error=500 (Server Error)&message=" + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.getSession().invalidate();
            response.setStatus(200);
            response.sendRedirect("/ims?signout=success");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.sendRedirect("/temperature-suite?error=500 (Server Error)&message=" + e.getMessage());
        }
    }
}
