/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import pl.pawelec.simpleemailclient.model.Email;
import pl.pawelec.simpleemailclient.model.enum_.Age;
import pl.pawelec.simpleemailclient.model.enum_.Folder;
import pl.pawelec.simpleemailclient.model.enum_.Status;
import pl.pawelec.simpleemailclient.qualifier.UserServiceQualifier;
import pl.pawelec.simpleemailclient.service.UserService;

/**
 *
 * @author mirek
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class EmaiJsonlMessageBodyReader implements MessageBodyReader<Email>{
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    
    @Inject @UserServiceQualifier
    private UserService userService;
    
    @Override
    public boolean isReadable(Class<?> type, 
                              Type genericType, 
                              Annotation[] annotations, 
                              MediaType mediaType) {
        return type == Email.class;
    }

    @Override
    public Email readFrom(Class<Email> type, 
                          Type genericType, 
                          Annotation[] annotations, 
                          MediaType mediaType, 
                          MultivaluedMap<String, String> httpHeaders, 
                          InputStream entityStream) throws IOException, WebApplicationException {
        Email email = new Email(); 
        JsonParser parser = Json.createParser(entityStream);
        while (parser.hasNext()) {
            switch (parser.next()) {
                case KEY_NAME:
                    String key = parser.getString();
                    parser.next();
                    switch (key) {
                        case "recipientEmail":     
                            email.setRecipient(parser.getString());
                            break;
                        case "additionalRecipientEmail":  
                            email.setAdditionalRecipient(parser.getString());
                            break;
                        case "title":  
                            email.setTitle(parser.getString());
                            break;
                        case "message":  
                            email.setMessage(parser.getString());
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
//            if(parser.next()==JsonParser.Event.KEY_NAME){
//                System.out.println( "parserGetKEY=" + parser.getString() );
//                parser.next();
//                System.out.println( "parserGetValue=" + parser.getString() );
//            }
        }
        email.setFolder(Folder.DRAFT.name());
        email.setFlag(Status.NO.name());
        email.setReadingConfirmation(Status.YES.name());
        email.setStatus(Age.OLD.name());
        return email;
    }
    
}
