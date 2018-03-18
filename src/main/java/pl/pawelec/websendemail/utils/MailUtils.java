/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.utils;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import pl.pawelec.websendemail.exception.EmailNoSentException;

/**
 *
 * @author mirek
 */
public class MailUtils {
    
    public static boolean send(String senderEmail,
                               String password,
                               String recipientEmail, 
                               String additionalRecipientEmail, 
                               String title,
                               String message) throws EmailNoSentException{ 
        //Setup system properties
        Properties properties = new Properties();     
        properties.put("mail.smtp.user", senderEmail);
        properties.put("mail.smtp.host", "smtp.wp.pl"); 
        properties.put("mail.smtp.port", "465");
//        properties.put("mail.debug", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.fallback", "false");  
        properties.put("mail.smtp.socketFactory.class",
                       "javax.net.ssl.SSLSocketFactory");

        //Instantiate mail session, compose email including subject, receipient & content
        Session mailSession = Session.getInstance(properties, 
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(senderEmail, password);
                    }  
                }
        );
        try {
            MimeMessage mailMsg = new MimeMessage(mailSession);
            mailMsg.setFrom(new InternetAddress(senderEmail));
            mailMsg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            if(!additionalRecipientEmail.isEmpty()){
                mailMsg.addRecipient(Message.RecipientType.CC, new InternetAddress(additionalRecipientEmail));
            }
            mailMsg.setSubject(title);
//            mailMsg.setContent(msg, "text/html");
            mailMsg.setText(message);
            Transport.send(mailMsg);
        } catch (MessagingException mex) {
            throw new EmailNoSentException("It has occurred an error while sending the email!");
        }
        return true;
    }
}
