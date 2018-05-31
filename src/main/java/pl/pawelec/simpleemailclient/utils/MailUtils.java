/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import pl.pawelec.simpleemailclient.exception.EmailNoSentException;
import pl.pawelec.simpleemailclient.model.Attachment;
import pl.pawelec.simpleemailclient.model.Email;
import pl.pawelec.simpleemailclient.model.EmailAccount;
import pl.pawelec.simpleemailclient.model.EmailInternetAddress;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.create.CreateInternetAddress;
import pl.pawelec.simpleemailclient.model.enum_.Age;
import pl.pawelec.simpleemailclient.model.enum_.MessageType;
import pl.pawelec.simpleemailclient.model.enum_.RecipientType;
import pl.pawelec.simpleemailclient.model.enum_.Status;

/**
 *
 * @author mirek
 */
public class MailUtils {    
    public static boolean send(Email email) throws EmailNoSentException{ 
        EmailAccount userAccount = email.getUser().getEmailAccount();
        try {
            Properties properties = new Properties();     
            properties.put("mail.smtp.user", userAccount.getEmailName());
            properties.put("mail.smtp.host", userAccount.getSmtpAddress()); 
            properties.put("mail.smtp.port", userAccount.getSmtpPortNo());
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.socketFactory.port", userAccount.getSmtpPortNo());
            properties.put("mail.smtp.socketFactory.fallback", "false");  
            properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//            properties.put("mail.debug", "true");
            
            Session mailSession = Session.getInstance(properties, 
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication(){
                        PasswordAuthentication auth = null;
                        try{
                            auth =  new PasswordAuthentication(
                                            userAccount.getEmailName(),
                                            userAccount.getEmailPassword());
                        }catch (IOException ex) {
                            System.err.println("PasswordAuthentication error:"+ex.getMessage());
                        }
                        return auth;
                    }  
                }
            );
            
            MimeMessage mailMsg = new MimeMessage(mailSession);
            mailMsg.setFrom(new InternetAddress(userAccount.getEmailName()));
            InternetAddress[] addressesTo = prepareArrayWithInternetAddress(email.getEmailInternetAddressesSet(), RecipientType.TO.name());
            if(addressesTo.length > 0){
                mailMsg.setRecipients(Message.RecipientType.TO, addressesTo);
            }
            InternetAddress[] addressesCc = prepareArrayWithInternetAddress(email.getEmailInternetAddressesSet(), RecipientType.CC.name());
            if(addressesCc.length > 0){
                mailMsg.setRecipients(Message.RecipientType.CC, addressesCc);
            }
            mailMsg.setSubject(email.getTitle(), "UTF-8");    
            if(email.getAttachments().isEmpty()){
                mailMsg.setContent(email.getMessage(), "text/html; charset=UTF-8");     // sending a html message
            }else{
                Multipart multipart = new MimeMultipart();
                BodyPart messagebodyPart = new MimeBodyPart();
                messagebodyPart.setContent(email.getMessage(), "text/html; charset=UTF-8");
                multipart.addBodyPart(messagebodyPart);
                Iterator<Attachment> iter = email.getAttachments().iterator();
                while(iter.hasNext()){
                    Attachment attachment = iter.next();
                    BodyPart attachmentBodyPart = new MimeBodyPart();
                    DataSource source = new ByteArrayDataSource(attachment.getContent(), attachment.getMimeType());
                    attachmentBodyPart.setDataHandler(new DataHandler(source));
                    attachmentBodyPart.setFileName(attachment.getName());
                    multipart.addBodyPart(attachmentBodyPart);
                }
                mailMsg.setContent(multipart);
            }
            Transport.send(mailMsg);
        } catch (MessagingException mex) {
            throw new EmailNoSentException("It has occurred an error while sending the email!");
        }
        return true;
    }
      
    private static InternetAddress[] prepareArrayWithInternetAddress(Set<EmailInternetAddress> emailInternetAddressSet, String recipientType){
        CreateInternetAddress createAddress = (a, b, c) -> {
            try {
                return new InternetAddress(a, b, c);
            } catch (UnsupportedEncodingException ex) {
                System.out.println("prepareArrayWithInternetAddress() : "+ex.getMessage());
            }
            return null;
        };
        InternetAddress[] addresses = emailInternetAddressSet.stream()
                .filter(a->a.getType().equals(recipientType))
                .map(a->a.getContact())
                .map(c->{
                    InternetAddress address = null;
                    try{
                        if( ! c.getName().isEmpty()){
                            address = new InternetAddress(c.getEmailAddress(), c.getName(), StandardCharsets.UTF_8.name());
                        }else{
                            address = new InternetAddress(c.getEmailAddress());
                        }
                    }catch(UnsupportedEncodingException | AddressException e){
                        System.out.println("prepareArrayWithInternetAddress() : "+e.getMessage());
                    }
                    return address;
                })
                .toArray(InternetAddress[]::new);
        System.out.println("prepareArrayWithInternetAddress() : recipientType="+recipientType);
        Arrays.stream(addresses).forEach(System.out::println); 
        return addresses;
    }
    
    public static List<Email> fetch(User user, Long lastMessageDate){
        List<Email> emails = new ArrayList<>();
        try{
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "pop3");
            properties.put("mail.pop3.host", user.getEmailAccount().getPop3Address());
            properties.put("mail.pop3.port", user.getEmailAccount().getPop3PortNo());
            properties.put("mail.pop3.starttls.enable", "true");
            Session session = Session.getDefaultInstance(properties);
            // session.setDebug(true);

            Store store = session.getStore("pop3s");
            store.connect(user.getEmailAccount().getPop3Address(), 
                          user.getEmailAccount().getEmailName(), 
                          user.getEmailAccount().getEmailPassword());
            
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            int numberAllMessages = folder.getMessageCount();
            System.out.println("Messages:" + numberAllMessages);
            for(int i = numberAllMessages; i>1; i--){
                System.out.println("Count email: "+(i-1)+"/"+numberAllMessages);
                Message message= null;
                try{
                    message = folder.getMessages()[i-1]; 
                    if(message.getSentDate().getTime()>lastMessageDate){
                        Email email = new Email.Builder().withUser(user)
                                .withSender(Optional.ofNullable(InternetAddress.toUnicodeString(message.getFrom())).orElse(""))
                                .withRecipient(Optional.ofNullable(InternetAddress.toUnicodeString(message.getRecipients(Message.RecipientType.TO))).orElse(""))
                                .withAdditionalRecipient(Optional.ofNullable(InternetAddress.toUnicodeString(message.getRecipients(Message.RecipientType.CC))).orElse(""))
                                .withTitle(Optional.ofNullable(message.getSubject()).orElse(""))
                                .withMessage("")
                                .withFolder(pl.pawelec.simpleemailclient.model.enum_.Folder.RECEIVED.name())
                                .withFlag(Status.NO.name())
                                .withReadingConfirmation(Status.NO.name())
                                .withStatus(Age.NEW.name())
                                .withMessageType(MessageType.INCOMING.name())
                                .withCreateDate(message.getSentDate().getTime())
                                .withAttachments(new HashSet<>()) 
                                .withEmailInternetAddressSet(new HashSet<>()) 
                                .build();
                        getContentFromMessage(message, email);
                        emails.add(email);
//                        System.out.println("fetch() : "+email);
                    }else{
                        break;
                    }
                }catch (MessagingException me) {
                    System.out.println("Can't get the message no "+(i-1)+"!\n The content error:"  + me.getMessage());
                }catch(Exception e){
                    System.out.println("Can't get the message no "+(i-1)+"!\n The content error:"  + e.getMessage());
                }
            } 
            folder.close(false);
            store.close();
        } catch (MessagingException ex) {
            System.out.println("fetch() : MessagingException="+ex.getMessage());
        } catch (Exception ex) {
            System.out.println("fetch() : Exception="+ex.getMessage());
        }
        return emails;
    }
    
    private static void getContentFromMessage(Part content, Email email) throws Exception{
//        if(content instanceof Message){
//            Message message = (Message) content;
//            System.out.println("--- New MESSAGE ---------------------"); 
//            System.out.println("contentType: " + message.getContentType()); 
//            System.out.println("from: " + InternetAddress.toUnicodeString(message.getFrom())); 
//            System.out.println("from: " + message.getSubject()); 
//        }
        if(content.getContentType().contains("text/plain") 
                && (content.getFileName()==null || content.getFileName().isEmpty())){
//            System.out.println("text/plain contentType: " + content.getContentType());
            String textMessage = Optional.ofNullable((String)content.getContent()).orElse("");
            if(email.getMessage().isEmpty()) email.setMessage(textMessage);
        }else if(content.getContentType().contains("text/html") 
                && (content.getFileName()==null || content.getFileName().isEmpty())){
//            System.out.println("text/html contentType: " + content.getContentType());
            String htmlMessage = Optional.ofNullable((String)content.getContent()).orElse("");
            if( ! htmlMessage.isEmpty()) email.setMessage(htmlMessage);
        }else if(content.getContentType().contains("multipart")){
            Multipart multipart = (Multipart) content.getContent();
            for(int i = 0; i<multipart.getCount(); i++){
                MimeBodyPart bodyPart = (MimeBodyPart) multipart.getBodyPart(i);
                getContentFromMessage(bodyPart, email);
            }
        }else{
            String fileName = !MimeUtility.decodeText(content.getFileName()).isEmpty()?MimeUtility.decodeText(content.getFileName()):"unknown";     
            String mimeType = content.getContentType().split("; ")[0];
            Integer size = content.getSize();
            InputStream is = content.getInputStream();
            byte[] buffor = new byte[is.available()];
            if(is.available()>0) is.read(buffor);
            is.close();
            email.addAttachment(new Attachment(fileName, buffor, mimeType, size.longValue()));
        }
        email.buildAttachmentName();
    }
    
    public static boolean deleteEmail(List<Object[]> emailsList){
        boolean deleteEmail = false, closeFolder = false; 
        EmailAccount emailAccount = ((Email) emailsList.get(0)[0]).getUser().getEmailAccount();
        Long theOldestDate = emailsList.stream().map(o->((Email)o[0]).getCreateDate()).min((a, b)->a.compareTo(b)).get();
        System.out.println("theOldestDate: " + Instant.ofEpochMilli(theOldestDate).atZone(ZoneId.systemDefault()).toLocalDateTime());
        List<Integer> emailIdToDelete = new ArrayList<>();
        try{
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "pop3");
            properties.put("mail.pop3s.host", emailAccount.getPop3Address());
            properties.put("mail.pop3s.port", emailAccount.getPop3PortNo());
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);
            // emailSession.setDebug(true);

            Store store = emailSession.getStore("pop3s");
            store.connect(emailAccount.getPop3Address(), emailAccount.getEmailName(), emailAccount.getEmailPassword());
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_WRITE);
            int numberOfMessages = emailFolder.getMessageCount();
            System.out.println("messages.length:" + numberOfMessages);
            try{
                for (int i = numberOfMessages; i > 1; i--) {
                    Message message = emailFolder.getMessages()[i-1];
                    String subject = message.getSubject();
                    long sentDate = Optional.ofNullable(message.getSentDate().getTime()).orElse(0L); 
                    if(sentDate<theOldestDate){
                        break;
                    }
                    System.out.println("sprawdzam: "+ (i-1) + "/" + numberOfMessages + ", data: " 
                            + Instant.ofEpochMilli(sentDate).atZone(ZoneId.systemDefault()).toLocalDateTime() + ", temat: " + subject);
                    for(Object[] o : emailsList){
                        Email email = (Email) o[0];
                        if(subject.equals(email.getTitle()) && sentDate==email.getCreateDate() && ((Boolean)o[1])){
                            emailIdToDelete.add(i-1);
                            break;
                        }
                    };
//                    System.out.println("---------------------------------");
//                    System.out.println("Email Number " + (i));
//                    System.out.println("Subject: " + subject);
//                    System.out.println("From: " + message.getFrom()[0]);
//                    System.out.println("SendDate: " + message.getSentDate().getTime());
                }
                emailIdToDelete.sort((a,b)->b.compareTo(a));
//                System.out.println("emailIdToDelete= " + emailIdToDelete);
                for(Integer id : emailIdToDelete){
                    System.out.println("usuwam id: "+id);
                    Message message = emailFolder.getMessages()[id];
                    message.setFlag(Flags.Flag.DELETED, true);
                };
                deleteEmail = true;
            }catch (Exception e) {
                e.printStackTrace(); 
            }           
            emailFolder.close(true);
            closeFolder = !emailFolder.isOpen();
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace(); 
        } catch (IOException ex) {
            ex.printStackTrace(); 
        }
//        System.out.println("deleted email? = " + (deleteEmail && closeFolder));
        return (deleteEmail && closeFolder);
    }
       
}
