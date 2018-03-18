/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.validation;

import java.math.BigDecimal;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author mirek
 */
@Provider
public class MailValidator implements ExceptionMapper<ConstraintViolationException>{

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(prepareJsonObjectWithErrors(exception))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    private String prepareJsonObjectWithErrors(ConstraintViolationException exception) {
        JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<String, Object>());
        JsonObjectBuilder jsonError = Json.createObjectBuilder();
        for(ConstraintViolation<?> error : exception.getConstraintViolations()){
            String field = error.getPropertyPath().toString();
            jsonError.add(field.substring(field.indexOf("arg")+5), error.getMessage());
        }
        return factory.createObjectBuilder().add("validationErrors", jsonError).build().toString();
    }
    
}
