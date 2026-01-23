package com.johnnyconsole.enterpriseims.api.classes;

import com.johnnyconsole.enterpriseims.persistence.interfaces.UserDao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("/auth")
@Stateless
public class AuthApi {

    @EJB
    UserDao userDao;

    @POST
    @Path("/sign-in")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Response signIn(@FormParam("username") String username,
                           @FormParam("password") String password) {

        if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
           return Response.status(BAD_REQUEST).build();
        }
        else if(!userDao.userExists(username)) {
            return Response.status(UNAUTHORIZED).build();
        }
           else if(!userDao.verifyUserPassword(userDao.getUser(username), password)) {
            return Response.status(UNAUTHORIZED).build();
        }
           else {
            return Response.ok(userDao.getUser(username)).build();
        }
    }
}
