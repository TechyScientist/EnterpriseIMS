package com.johnnyconsole.enterpriseims.api.classes;

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

@Path("/user")
@Stateless
public class UserApi {

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
                           @FormParam("is-admin") boolean isAdmin,
                           @FormParam("auth-user") String authUser) {

        //TODO: Add implementation from AddUserServlet
        return Response.ok().build();
    }

    @POST
    @Path("/delete")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Response delete(@FormParam("username") String username,
                           @FormParam("authUser") String authUser) {

        //TODO: Add implementation from DeleteUserServlet
        return Response.ok().build();
    }
}
