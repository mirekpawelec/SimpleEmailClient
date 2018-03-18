/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import pl.pawelec.websendemail.model.Mail;

/**
 *
 * @author mirek
 */
public class MailMessageBodyWriterJson implements MessageBodyWriter<Mail>{ 

    @Override
    public boolean isWriteable(Class<?> type, 
            Type genericType, 
            Annotation[] annotations, 
            MediaType mediaType) {
        return false; //type == Mail.class;
    }

    @Override
    public long getSize(Mail t, 
            Class<?> type, 
            Type genericType, 
            Annotation[] annotations, 
            MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Mail t, 
            Class<?> type, 
            Type genericType, 
            Annotation[] annotations, 
            MediaType mediaType, 
            MultivaluedMap<String, Object> httpHeaders, 
            OutputStream entityStream) throws IOException, WebApplicationException {
        
    }
    
}
