/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.exception;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import pl.pawelec.websendemail.model.ResponseError;

/**
 *
 * @author mirek
 */
@Provider
public class EmailNoSentExceptionMapper implements ExceptionMapper<Exception>{ 

    @Override
    public Response toResponse(Exception exception) {
        ResponseError responseError = new ResponseError();
        String defaultMsgError = "It's occurred an internal error service!";
        if(exception instanceof EmailNoSentException){
            responseError.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            responseError.setMessage(exception.getMessage());
        } else {
            responseError.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            responseError.setMessage(defaultMsgError + " " + exception.getMessage());
        }
        JsonObject jsonError = Json.createObjectBuilder().add("error", responseError.getMessage()).build();
        return Response.status(responseError.getCode()).entity(jsonError.toString()).type(MediaType.APPLICATION_JSON_TYPE).build(); 
    }
    
}
