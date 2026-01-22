package com.johnnyconsole.enterpriseims.api.classes;

import com.johnnyconsole.enterpriseims.persistence.User;
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
        int status = 200;
        String json;
        if (username != null && password != null &&
                !username.isEmpty() && !password.isEmpty()) {
            if (userDao.userExists(username)) {
                User user = userDao.getUser(username);

                if (userDao.verifyUserPassword(user, password)) {
                    json = "{\n\t" +
                            "\"username\": \"" + user.getUsername() + "\",\n\t" +
                            "\"name\": \"" + user.getName() + "\",\n\t" +
                            "\"is_administrator\": " + user.isAdministrator() + "\n" +
                            "}";
                } else {
                    status = 401;
                    json = "{\n\t" +
                            "\"error\": \"401 (Unauthorized)\",\n\t" +
                            "\"message\": \"Invalid credentials, please try again\"\n" +
                            "}";
                }
            } else {
                status = 401;
                json = "{\n\t" +
                        "\"error\": \"401 (Unauthorized)\",\n\t" +
                        "\"message\": \"Invalid credentials, please try again.\"\n" +
                        "}";
            }
        } else {
            status = 400;
            json = "{\n\t" +
                    "\"error\": \"400 (Bad Request)\",\n\t" +
                    "\"message\": \"Missing username or password.\"\n" +
                    "}";
        }

        return Response.status(status).entity(json).build();
    }
}
