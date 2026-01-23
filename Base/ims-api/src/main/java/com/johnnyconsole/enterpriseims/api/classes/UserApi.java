package com.johnnyconsole.enterpriseims.api.classes;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.johnnyconsole.enterpriseims.persistence.User;
import com.johnnyconsole.enterpriseims.persistence.interfaces.UserDao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_IMPLEMENTED;

@Path("/user")
@Stateless
public class UserApi {
    //The HTTP code 422 Unprocessable Content is used if the user's password
    //doesn't match the confirmation text.
    public static final int UNPROCESSABLE = 422;

    @EJB
    UserDao userDao;

    @GET
    @Path("/get")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Response get(@QueryParam("username")  String username) {
        if(username == null || username.isEmpty()) {
            return Response.status(BAD_REQUEST).build();
        }
        else if(!userDao.userExists(username)) {
            return Response.status(NOT_FOUND).build();
        }
        else {
            return Response.ok(userDao.getUser(username)).build();
        }
    }

    @POST
    @Path("/add")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Response add(@FormParam("username") String username,
                           @FormParam("name") String name,
                           @FormParam("password") String password,
                           @FormParam("confirm-password") String confirmPassword,
                           @FormParam("is-admin") boolean isAdmin,
                           @FormParam("auth-user") String authUser) {

        if(username == null || username.isEmpty() ||
                name == null || name.isEmpty() ||
                password == null || password.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty() ||
                authUser == null || authUser.isEmpty()) {
            return Response.status(BAD_REQUEST).build();
        }
        else if(!userDao.userExists(authUser)) {
            return Response.status(NOT_FOUND).build();
        }
        else if(!userDao.getUser(authUser).isAdministrator()) {
            return Response.status(UNAUTHORIZED).build();
        }
        else if(userDao.userExists(username)) {
            return Response.status(CONFLICT).build();
        }
        else if(!password.equals(confirmPassword)) {
            return Response.status(UNPROCESSABLE).build();
        }
        else {
            userDao.addUser(new User(
                    username, name,
                    BCrypt.withDefaults().hashToString(12, password.toCharArray()),
                    isAdmin));
            return Response.status(CREATED).build();
        }
    }

    @POST
    @Path("/delete")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Response delete(@FormParam("username") String username,
                           @FormParam("authUser") String authUser) {

        //TODO: Add implementation from DeleteUserServlet
        return Response.status(NOT_IMPLEMENTED).build();
    }
}
