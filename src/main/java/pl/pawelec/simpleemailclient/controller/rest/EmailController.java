/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.controller.rest;


import java.awt.BorderLayout;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import pl.pawelec.simpleemailclient.exception.EmailNoFoundException;
import pl.pawelec.simpleemailclient.exception.EmailNoSentException;
import pl.pawelec.simpleemailclient.model.Email;
import pl.pawelec.simpleemailclient.model.EmailInternetAddress;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.enum_.Folder;
import pl.pawelec.simpleemailclient.model.enum_.RecipientType;
import pl.pawelec.simpleemailclient.model.enum_.Status;
import pl.pawelec.simpleemailclient.qualifier.EmailInternetAddressServiceQualifier;
import pl.pawelec.simpleemailclient.qualifier.EmailServiceQualifier;
import pl.pawelec.simpleemailclient.qualifier.UserServiceQualifier;
import pl.pawelec.simpleemailclient.service.EmailInternetAddressService;
import pl.pawelec.simpleemailclient.service.EmailService;
import pl.pawelec.simpleemailclient.service.UserService;
import pl.pawelec.simpleemailclient.utils.Encryption;

/**
 *
 * @author mirek
 */
@Path("/emails")
public class EmailController {
    private static final String CONFIRMATION_SENDING_EMAIL = "The email has been sent!";
    private static final String ERROR_SENDING_EMAIL = "The email has not been sent!";
    private static final String NO_DATA_FOUND = "Folder jest pusty.";
    @Inject @UserServiceQualifier
    private UserService userServiceImpl;
    @Inject @EmailServiceQualifier
    private EmailService emailServiceImpl;
    @Inject @EmailInternetAddressServiceQualifier
    private EmailInternetAddressService emailInternetAddressService;
    @Context
    private HttpServletRequest httpServletRequest;

    
    @RolesAllowed({"ADMIN"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        ResponseBuilder rb = Response.ok();
        rb.entity(convertMailListToMapJson(emailServiceImpl.getAll()));
        rb.type(MediaType.APPLICATION_JSON_TYPE);
        return rb.build();
    }
    
    private String convertMailListToMapJson(List<Email> emailList){
        JsonArrayBuilder jsonArrMails = Json.createArrayBuilder();
        emailList.forEach( m -> {jsonArrMails.add(Json.createObjectBuilder()
                                .add("ID", m.getId())
                                .add("FROM", m.getUser().getEmailAccount().getEmailName())
                                .add("TO", m.getRecipient())
                                .add("CC", m.getAdditionalRecipient())
                                .add("TITLE", m.getTitle())
                                .add("CONTENTS", m.getMessage())
                                .add("FOLDER", m.getFolder())
                                .add("FLAG", m.getFlag())
                                .add("READ", m.getReadingConfirmation())
                                .add("DATE", m.getCreateDate())
                                .build());                   
        });
        JsonArray jsonMails = jsonArrMails.build();
        return jsonMails.toString();
    }
    
    @RolesAllowed({"ADMIN"})
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id){
        return Response.ok(emailServiceImpl.get(id)).build();
    }
    
    @RolesAllowed({"ADMIN","USER"})
    @GET 
    @Path("/folder/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmailsByFolder(@PathParam("name") String folderName, 
                                      @QueryParam("checkServer") @DefaultValue("false") Boolean checkServer,
                                      @QueryParam("page") @DefaultValue("1") Integer page,
                                      @QueryParam("limit") @DefaultValue("15") Integer limit,
                                      @Context Request request){
        JsonArrayBuilder jsonEmailList = Json.createArrayBuilder();
        List<Email> emailsList = new ArrayList<>();
        User user = userServiceImpl.getUserFromRequest(httpServletRequest);
        if(folderName.equalsIgnoreCase(Folder.RECEIVED.name()) && checkServer){
            emailServiceImpl.checkEmailBox(user);
        }
        try{
            emailsList = emailServiceImpl.getByFolder(folderName, user.getId(), page, limit);
        }catch(EmailNoFoundException enfe){
            jsonEmailList.add("{\"no_data_found\":\""+NO_DATA_FOUND+"\"}");
        }
        EntityTag entityTag = new EntityTag(String.valueOf(emailsList.hashCode()));
        ResponseBuilder responseBuilder = request.evaluatePreconditions(entityTag);
        if(responseBuilder!=null){
            return responseBuilder.build();
        }else{
            responseBuilder = Response.ok();
            if( ! emailsList.isEmpty()){
                emailsList.forEach(e->{
                    e.setSender(prepareChainEmailAddress(e.getEmailInternetAddressesSet(), RecipientType.FROM.name()));
                    e.setRecipient(prepareChainEmailAddress(e.getEmailInternetAddressesSet(), RecipientType.TO.name()));
                    e.setAdditionalRecipient(prepareChainEmailAddress(e.getEmailInternetAddressesSet(), RecipientType.CC.name()));
                    ObjectMapper mapper = new ObjectMapper();
                    String json = "";
                    try {
                        json = mapper.writeValueAsString(e);
                    } catch (IOException ex) {
                        System.out.println("Error while convert an object to json: "+ex.getMessage());
                    }
                    jsonEmailList.add(json);
                });  
            }
            Long count = emailServiceImpl.count(user.getId(), folderName);
            Map<String, Object> config = new HashMap<>();
            JsonBuilderFactory factoryJsonObjects = Json.createBuilderFactory(config);
            int previousPage = page-1==0?1:page-1;
            int nextPage = page*limit>=count?page:page+1;
            long allPages = (count/limit) + ((count%limit)>0?1:0);
            JsonObject jsonEmails = factoryJsonObjects.createObjectBuilder()
                    .add("metadata", factoryJsonObjects.createObjectBuilder()
                        .add("resultsPerPage", emailsList.size())
                        .add("totalCount", count)
                        .add("currentPage", count==0?0:page)
                        .add("allPages", allPages)
                        .add("links", factoryJsonObjects.createArrayBuilder()
                            .add(factoryJsonObjects.createObjectBuilder()
                                    .add("previous", "rest/emails/folder/"+folderName+"?page="+previousPage+"&limit=15"))
                            .add(factoryJsonObjects.createObjectBuilder()
                                    .add("next", "rest/emails/folder/"+folderName+"?page="+nextPage+"&limit=15"))))
                    .add("emails", jsonEmailList)
                    .build(); 
            responseBuilder.entity(jsonEmails.toString());
            responseBuilder.type(MediaType.APPLICATION_JSON_TYPE);
            responseBuilder.tag(entityTag);
            return responseBuilder.build();
        }
    }
    
    private static String prepareChainEmailAddress(Set<EmailInternetAddress> contacts, String recipientType){
        return contacts.stream()
                    .filter(ia->ia.getType().equals(recipientType))
                    .map(ia->ia.getContact().concatenateNameWithEmailAddress())
                    .reduce((a, b)->a+", "+b)
//                    .map(s->s.substring(0, s.length()-1))
                    .orElse("");
    }
    
    @RolesAllowed({"ADMIN"})
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Email add(@Valid Email mail){
        mail.setFolder(Folder.RECEIVED.name());
        mail.setReadingConfirmation(Status.NO.name()); 
        Long id = emailServiceImpl.create(mail).getId();
        return emailServiceImpl.get(id);
    }
    
    @RolesAllowed({"ADMIN","USER"})
    @POST
    @Path("/send")
    @Produces(MediaType.APPLICATION_JSON)
    public Response send(@Valid Email email){
        JsonObject processStatus = null;
        emailServiceImpl.create(email);
        email.setEmailInternetAddressesSet(emailInternetAddressService.getSetByEmailId(email.getId()));
        if(emailServiceImpl.send(email)){  
            processStatus = Json.createObjectBuilder()
                    .add("success", CONFIRMATION_SENDING_EMAIL)
                    .build();
            email.setMessage(Encryption.encodeBase64(email.getMessage())); 
            email.setFolder(Folder.SENT.name());
            emailServiceImpl.update(email);
        } else {
            throw new EmailNoSentException(ERROR_SENDING_EMAIL);
//            processStatus = Json.createObjectBuilder()
//                    .add("error", ERROR_SENDING_EMAIL)
//                    .build();
        }
        return Response.ok()
                .entity(processStatus.toString())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();  
    }  
    
    @RolesAllowed({"ADMIN","USER"})
    @PUT
    @Path("/delete/single/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteSingleEmail(@PathParam("id") Long id){
        ResponseBuilder rb = Response.ok();
        Email mail = emailServiceImpl.get(id);
        rb.entity(emailServiceImpl.delete(mail));
        rb.type(MediaType.TEXT_PLAIN_TYPE);
        return rb.build();
    }
    
    @RolesAllowed({"ADMIN","USER"})
    @PUT
    @Path("/delete/group")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteGroupEmails(@MatrixParam("ids") String ids, 
                                      @MatrixParam("permanent") Boolean permanentDelete){
        List<Object[]> toDeleteEmailsList = new ArrayList<>();
        JsonArrayBuilder jsonArr = Json.createArrayBuilder();
        Pattern.compile(",").splitAsStream(ids).map(Long::new).forEach(id -> {
            toDeleteEmailsList.add(new Object[]{emailServiceImpl.get(id), false});
        });
        if(permanentDelete){
            emailServiceImpl.deletePermanently(toDeleteEmailsList).stream().forEach(o->{
                Long id = ((Email)o[0]).getId();
                jsonArr.add(Json.createObjectBuilder()
                                .add("id", String.valueOf(id))
                                .add("deleted", ((Boolean) o[1])?"true":"false")
                                .build());
            });
        }else{
            toDeleteEmailsList.stream().forEach(o->{
                Email email = (Email) o[0];
                jsonArr.add(Json.createObjectBuilder()
                                .add("id", String.valueOf(email.getId()))
                                .add("deleted", emailServiceImpl.delete(email)?"true":"false")
                                .build());
            });
        }     
        JsonArray result = jsonArr.build();
        ResponseBuilder rb = Response.ok();
        rb.entity(result.toString());
        rb.type(MediaType.APPLICATION_JSON_TYPE);
        return rb.build();
    }
    
    @RolesAllowed({"ADMIN","USER"})
    @PUT
    @Path("/move/{folder}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response moveToFolder(@PathParam("folder") String folderName,
                                 @MatrixParam("ids") String ids){
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        Stream.of(ids.split(",")).forEach(id->{
            try{
                Email email = emailServiceImpl.get(Long.valueOf(id));
                email.setFolder(folderName.toUpperCase());
                if(emailServiceImpl.update(email)){
                    jsonArray.add(Json.createObjectBuilder()
                                    .add("id", id)
                                    .add("moved", "true")
                                    .build());
                }else{
                    jsonArray.add(Json.createObjectBuilder()
                                    .add("id", id)
                                    .add("moved", "false")
                                    .build());
                }
            }catch(EmailNoFoundException enfe){
                jsonArray.add(Json.createObjectBuilder()
                                .add("id", id)
                                .add("moved", "false")
                                .build());
            }
        });
        JsonArray jsonResult = jsonArray.build();
        ResponseBuilder rb = Response.ok();
        rb.entity(jsonResult.toString());
        rb.type(MediaType.APPLICATION_JSON_TYPE);
        return rb.build();
    }
    
    @RolesAllowed({"ADMIN","USER"})
    @PUT
    @Path("/{id}/flag")
    @Produces(MediaType.TEXT_PLAIN)
    public Response changeFlagMessage(@PathParam("id") Long id){
        return Response.ok(emailServiceImpl.changeFlag(id))
                .type(MediaType.TEXT_PLAIN_TYPE).build();
    }
    
    @RolesAllowed({"ADMIN","USER"})
    @POST
    @Path("/{id}/read")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response changeReadStatusMessage(@PathParam("id") Long id, 
                                            @FormParam("status") String status){
        emailServiceImpl.changeReadStatus(id, status);
        return Response.ok().build();
    }
    
    @RolesAllowed({"ADMIN", "USER"})
    @PUT
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStatus(@MatrixParam("ids") String ids){
        JsonArrayBuilder jsonArr = Json.createArrayBuilder();
        Stream.of(ids.split(",")).forEach(id -> {
            if(emailServiceImpl.changeStatus(Long.valueOf(id))){
                jsonArr.add(Json.createObjectBuilder()
                                .add("id", id)
                                .add("changed", "true")
                                .build());
            }else{
                jsonArr.add(Json.createObjectBuilder()
                                .add("id", id)
                                .add("deleted", "false")
                                .build());
            }
        });
        JsonArray result = jsonArr.build();
        ResponseBuilder rb = Response.ok();
        rb.entity(result.toString());
        rb.type(MediaType.APPLICATION_JSON_TYPE);
        return rb.build();
    }
}
