package com.johnnyconsole.enterpriseims.api.classes;

import com.johnnyconsole.enterpriseims.persistence.interfaces.UserDao;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/auth")
@RequestScoped
public class AuthApi {

    @EJB
    UserDao userDao;

    @POST
    @Path("/sign-in")
    @Produces(APPLICATION_JSON)
    public Response signIn(@FormParam("username") String username, @FormParam("password") String password) {
        //TODO: Check entered information and build the response object
        return Response.ok().build();
    }
}
