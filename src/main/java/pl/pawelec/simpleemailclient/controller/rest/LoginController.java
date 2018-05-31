/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.controller.rest;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.util.Base64;
import pl.pawelec.simpleemailclient.qualifier.UserServiceQualifier;
import pl.pawelec.simpleemailclient.service.UserService;

/**
 *
 * @author mirek
 */
@Path("/logins")
public class LoginController {
    @Inject @UserServiceQualifier
    private UserService userService;
    
    @PermitAll
    @GET
    @Path("/{name}/exists")
    @Produces(MediaType.TEXT_PLAIN)
    public Response checkUserLogin(@PathParam("name") String name){
        ResponseBuilder rb = null;
        try {
            String decodeName = URLDecoder.decode(new String(Base64.decode(name)), "UTF-8");
            try{
                rb = Response.ok();
                userService.getByLogin(decodeName);
                rb.entity(true);
            }catch(NoResultException nre){
                rb.entity(false);
            }
        } catch (IOException ex) {
            rb.status(Response.Status.INTERNAL_SERVER_ERROR);
            rb.entity("It's failed decode the user login");
        }
        rb.type(MediaType.TEXT_PLAIN_TYPE);
        return rb.build();
    }
}
