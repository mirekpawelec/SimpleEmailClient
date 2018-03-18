/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import pl.pawelec.websendemail.model.Mail;
import pl.pawelec.websendemail.model.enum_.Folder;
import pl.pawelec.websendemail.model.enum_.Status;

/**
 *
 * @author mirek
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class MailMessageBodyReaderJson implements MessageBodyReader<Mail>{

    @Override
    public boolean isReadable(Class<?> type, 
                              Type genericType, 
                              Annotation[] annotations, 
                              MediaType mediaType) {
        return type == Mail.class;
    }

    @Override
    public Mail readFrom(Class<Mail> type, 
                         Type genericType, 
                         Annotation[] annotations, 
                         MediaType mediaType, 
                         MultivaluedMap<String, String> httpHeaders, 
                         InputStream entityStream) throws IOException, WebApplicationException {
        Mail mail = new Mail();
        JsonParser parser = Json.createParser(entityStream);
        while (parser.hasNext()) {
            switch (parser.next()) {
                case KEY_NAME:
                    String key = parser.getString();
                    parser.next();
                    switch (key) {
                        case "recipientEmail":     
                            mail.setRecipientEmail(parser.getString());
                            break;
                        case "additionalRecipientEmail":  
                            mail.setAdditionalRecipientEmail(parser.getString());
                            break;
                        case "title":  
                            mail.setTitle(parser.getString());
                            break;
                        case "message":  
                            mail.setMessage(parser.getString());
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
        mail.setFolder(Folder.SENT.name());
        mail.setImportantMessage(Status.NO.name());
        mail.setReadingConfirmation(Status.YES.name());
        return mail;
    }
    
}
