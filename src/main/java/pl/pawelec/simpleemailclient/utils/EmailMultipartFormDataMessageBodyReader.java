/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import pl.pawelec.simpleemailclient.model.Attachment;
import pl.pawelec.simpleemailclient.model.Email;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.enum_.Age;
import pl.pawelec.simpleemailclient.model.enum_.DefaultHtmlAttachmentName;
import pl.pawelec.simpleemailclient.model.enum_.EmailSeparator;
import pl.pawelec.simpleemailclient.model.enum_.MessageType;
import pl.pawelec.simpleemailclient.model.enum_.PathToFile;
import pl.pawelec.simpleemailclient.model.enum_.SendEmailForm;
import pl.pawelec.simpleemailclient.model.enum_.Status;
import pl.pawelec.simpleemailclient.qualifier.UserServiceQualifier;
import pl.pawelec.simpleemailclient.service.UserService;

/**
 *
 * @author mirek
 */
@Provider
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class EmailMultipartFormDataMessageBodyReader implements MessageBodyReader<Email>{
    private static final String GETTING_VALUE_FROM_FILES_ERROR = "The process getting value from files of form has been failed!";
    private static final String UNKNOWN_ERROR = "Unknown an error: ";
    @Inject @UserServiceQualifier
    private UserService userServiceImpl;
    @Context
    private HttpServletRequest request;
    
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type==Email.class;
    }

    @Override
    public Email readFrom(Class<Email> type, Type genericType, Annotation[] annotations, MediaType mediaType, 
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        String idPreviousEmail = ""; //request.getParameter("previousEmail");
        User user = userServiceImpl.getUserFromRequest(request);
        Email email = new Email();
        email.setUser(user);
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                for(FileItem item : multiparts){
                    if(item.isFormField()){
                        if(item.getFieldName().equals(SendEmailForm.ID.getName())){
                            idPreviousEmail = new String(item.get(), StandardCharsets.UTF_8.name()); 
                        }else if(item.getFieldName().equals(SendEmailForm.SENDER.getName())){
                            email.setSender( new String(item.get(), StandardCharsets.UTF_8.name()) ); 
                        }else if(item.getFieldName().equals(SendEmailForm.RECIPIENT.getName())){
                            email.setRecipient( new String(item.get(), StandardCharsets.UTF_8.name()) ); 
                        }else if(item.getFieldName().equals(SendEmailForm.ADDITIONAL_RECIPIENT.getName())){
                            email.setAdditionalRecipient( new String(item.get(), StandardCharsets.UTF_8.name()) ); 
                        }else if(item.getFieldName().equals(SendEmailForm.TITLE.getName())){
                            email.setTitle( new String(item.get(), StandardCharsets.UTF_8.name()) ); 
                        }else if(item.getFieldName().equals(SendEmailForm.MESSAGE.getName())){
                            email.setMessage( new String(item.get(), StandardCharsets.UTF_8.name()) ); 
                        }else if(item.getFieldName().equals(SendEmailForm.ATTACHMENT_NAME.getName())){
                            email.setAttachmentName(new String(item.get(), StandardCharsets.UTF_8.name()) ); 
                        }
                    }else{
                        if(item.getFieldName().contains(SendEmailForm.ATTACHMENTS.getName())){
                            Attachment attachment = new Attachment();
                            attachment.setName(new String(item.getName().getBytes(), StandardCharsets.UTF_8.name()));
                            attachment.setContent(item.get());
                            attachment.setMimeType(item.getContentType());
                            attachment.setSize(item.getSize());
                            email.addAttachment(attachment);
                        }
                    }
                }
            } catch (FileUploadException ex) {
                System.out.println(GETTING_VALUE_FROM_FILES_ERROR+"\n"+ex.getMessage());
            } catch (Exception ex) {
                System.out.println(UNKNOWN_ERROR + ex.getMessage());
            }
        }
        if(idPreviousEmail!=null && !idPreviousEmail.isEmpty()){
            for(Attachment a : getPreviousAttachment(email.getUser().getId().toString(), idPreviousEmail, email.getAttachmentName())){
                if( ! a.getName().equals(DefaultHtmlAttachmentName.FILE_NAME.getName())){   // it different from the file name text.html
                    email.addAttachment(a);
                }
            }
        }
        email.buildAttachmentName();
        email.setReadingConfirmation(Status.YES.name());
        email.setStatus(Age.OLD.name());
        email.setMessageType(MessageType.OUTGOING.name());
        return email;
    } 
    
    private Set<Attachment> getPreviousAttachment(String userId, String emailId, String attachmentNamesString){
        Set<Attachment> attachmentsSet = new HashSet<>();
        String[] attachmentNames = attachmentNamesString.split(EmailSeparator.REG_EX.getSeparator());
        for(String attachmentName : attachmentNames){
            if(attachmentName.length()>0){
                Path pathToFile = Paths.get(PathToFile.MAIN_REPOSITORY.getPath(),userId,emailId,attachmentName);
                File file = new File(pathToFile.toUri());
                if(file.isFile()){
                    Attachment attachment = new Attachment();
                    try {
                        attachment.setName(file.getName());  
                        attachment.setContent(Files.readAllBytes(pathToFile));
                        attachment.setMimeType(Files.probeContentType(pathToFile));
                        attachment.setSize(Files.size(pathToFile));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    attachmentsSet.add(attachment);
                }
            }
        }
        return attachmentsSet;
    }

}
