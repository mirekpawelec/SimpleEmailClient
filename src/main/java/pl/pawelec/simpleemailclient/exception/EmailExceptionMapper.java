/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.exception;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import pl.pawelec.simpleemailclient.model.ResponseError;

/**
 *
 * @author mirek
 */
@Provider
public class EmailExceptionMapper implements ExceptionMapper<Exception>{ 
    private static final String JSON_FIELD_NAME = "error";
    private static final String DEFAULT_MSG_ERROR = "It's occurred an internal error service!";
    @Override
    public Response toResponse(Exception exception) {
        ResponseError responseError = new ResponseError();
        if(exception instanceof EmailNoSentException 
                || exception instanceof EmailNoFoundException
                || exception instanceof FlagNoChangeException 
                || exception instanceof ReadStatusNoChangeException 
                || exception instanceof EmailCanNotDeleteException
                || exception instanceof FileNoFoundException
                || exception instanceof EmailNoSentException){
            responseError.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            responseError.setMessage(exception.getMessage());
        }else {
            responseError.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            responseError.setMessage(DEFAULT_MSG_ERROR + " " + exception.getMessage());
        }
        JsonObject jsonError = Json.createObjectBuilder()
                .add(JSON_FIELD_NAME, responseError.getMessage())
                .build();
        return Response.status(responseError.getCode())
                .entity(jsonError.toString())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build(); 
    }
    
}
