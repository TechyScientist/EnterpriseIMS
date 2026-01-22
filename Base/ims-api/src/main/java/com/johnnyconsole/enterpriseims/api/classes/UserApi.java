package com.johnnyconsole.enterpriseims.api.classes;

import com.johnnyconsole.enterpriseims.persistence.User;
import com.johnnyconsole.enterpriseims.persistence.interfaces.UserDao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/user")
@Stateless
public class UserApi {

    @EJB
    UserDao userDao;

    @POST
    @Path("/add")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Response add(@FormParam("username") String username,
                           @FormParam("name") String name,
                           @FormParam("password") String password,
                           @FormParam("is-admin") boolean isAdmin) {
        int status = 200;
        String json = "{\"status\": \"ok\"}";
        //TODO: Add implementation from AddUserServlet
        return Response.status(status).entity(json).build();
    }

    @POST
    @Path("/delete")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Response delete(@FormParam("username") String username,
                           @FormParam("admin-username") String adminUsername) {
        int status = 200;
        String json = "{\"status\": \"ok\"}";
        //TODO: Add implementation from DeleteUserServlet
        return Response.status(status).entity(json).build();
    }
}
