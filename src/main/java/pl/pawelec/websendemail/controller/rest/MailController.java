/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.controller.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import pl.pawelec.websendemail.exception.EmailNoSentException;
import pl.pawelec.websendemail.model.Mail;
import pl.pawelec.websendemail.service.MailService;
import pl.pawelec.websendemail.service.impl.MailServiceImpl;

/**
 *
 * @author mirek
 */
@Path("/emails")
public class MailController {
    MailService mailService = new MailServiceImpl();
    private static final String confirmationSendingEmail = "Wiadomość została wysłana."; //"The email has been sent!";
    private static final String errorSendingEmail = "Wiadomość nie została wysłana."; //"The email has not been sent!";
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        ResponseBuilder rb = Response.ok();
        rb.entity( convertMailListToMapJson(mailService.getAll()) );
        rb.type(MediaType.APPLICATION_JSON_TYPE);
        return rb.build();
    }
    
    private String convertMailListToMapJson(List<Mail> listMail){
        JsonArrayBuilder jsonArrMails = Json.createArrayBuilder();
        listMail.forEach( m -> {jsonArrMails.add(Json.createObjectBuilder()
                                .add("ID", m.getId())
                                .add("TO", m.getRecipientEmail())
                                .add("CC", m.getAdditionalRecipientEmail())
                                .add("TITLE", m.getTitle())
                                .add("CONTENTS", m.getMessage())
                                .add("FOLDER", m.getFolder())
                                .add("FLAG", m.getImportantMessage())
                                .add("READ", m.getReadingConfirmation())
                                .add("DATE", m.getCreateDate())
                                .build());                   
        });
        JsonArray jsonMails = jsonArrMails.build();
        return jsonMails.toString();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mail get(@PathParam("id") Integer id){
        return mailService.get(id);
    }
    
    @GET
    @Path("/folder/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmailsByFolder(@PathParam("name") String name, @Context Request request){
        List<Mail> emailList = mailService.getAll().stream().filter(m->m.getFolder().equals(name.toUpperCase())).collect(Collectors.toList());
        EntityTag entityTag = new EntityTag(emailList.hashCode()+"");
        Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(entityTag);
        if(responseBuilder!=null){
            return responseBuilder.build();
        }else{
            responseBuilder = Response.ok().entity(emailList);
            responseBuilder.type(MediaType.APPLICATION_JSON_TYPE);
            responseBuilder.tag(entityTag);
            return responseBuilder.build();
        }
    }
    
    @OPTIONS
    @Path("/folder/{name}")
    public Response optionsUriForGetEmailByFolderMethod() {
       return Response.ok().header(HttpHeaders.ALLOW, HttpHeaders.IF_NONE_MATCH).build();
    }
    
    @POST
    @Path("/send")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response send(@Valid Mail mail){
        JsonObject processStatus;
        int id = mailService.save(mail);
        mailService.sendEmail(mail);
        Mail sentEmail = mailService.get(id);
        if(sentEmail!=null){
            processStatus = Json.createObjectBuilder().add("success", confirmationSendingEmail).build();
        } else {
            processStatus = Json.createObjectBuilder().add("error", errorSendingEmail).build();
        }
        return Response.ok()
                .entity(processStatus.toString())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();  
    }  
    
    @OPTIONS
    @Path("/send")
    public Response optionsUriForAddMethod() {
       return Response.ok().header(HttpHeaders.ALLOW, HttpMethod.POST).build();
    }
    
    @GET
    @Path("/jsonp")
    @Produces("text/javascript")
    public Response getDataByJSONP(@QueryParam("callback") String methodName){
        return Response.ok().entity(methodName + "({\"firstName\":\"Andrzej\",\"lastName\":\"Kowalski\",\"age\":60})").type("text/javascript").build();
    }
    
}
