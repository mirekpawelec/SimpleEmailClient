/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.validation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.hibernate.Session;
import org.jboss.resteasy.util.Base64;
import pl.pawelec.simpleemailclient.configuration.HibernateUtil;
import pl.pawelec.simpleemailclient.model.Email;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.dao.EmailDao;
import pl.pawelec.simpleemailclient.model.dao.UserDao;
import pl.pawelec.simpleemailclient.model.dao.impl.EmailDaoImpl;
import pl.pawelec.simpleemailclient.model.dao.impl.UserDaoImpl;
import pl.pawelec.simpleemailclient.model.enum_.Age;
import pl.pawelec.simpleemailclient.model.enum_.Status;
import pl.pawelec.simpleemailclient.qualifier.EmailServiceQualifier;
import pl.pawelec.simpleemailclient.service.EmailService;
import pl.pawelec.simpleemailclient.service.UserService;
import pl.pawelec.simpleemailclient.service.impl.UserServiceImpl;

/**
 *
 * @author mirek
 */
public class test {
    
    public static void main(String[] args) {

        
            //        UserDao us = new UserDaoImpl();
            
//        System.out.println(us.getByLogin("mirek"));

//        Session session = HibernateUtil.getSessionFactory().openSession();
//        System.out.println(session.createQuery("FROM Email").list());
//        session.close();
        
//        System.out.println( LocalDateTime.now().toInstant(ZoneOffset.ofHours(1)).toEpochMilli());
//        User user = new User();
//        user.setId(1l);
//        System.out.println(new Email(user, "dupa@wp.pl", "dupa@wp.pl", "dupa@wp.pl", "dupa", "text", "SENT"));
//        System.out.println(
//                new Email.Builder()
//                .withUser(user)
//                .withSender("pizda@wp.pl")
//                .withRecipient("pizda@wp.pl")
//                .withAdditionalRecipient("pizda@wp.pl")
//                .withTitle("tytuł")
//                .withMessage("text")
//                .withFolder("DRAFT")
//                .withFlag(Status.NO.name())
//                .withReadingConfirmation(Status.YES.name())
//                .withStatus(Age.OLD.name())
//                .withCreateDate(System.currentTimeMillis())
//                .withAttachmentName("")
////                .withAttachmentFile(new byte[0])
//                .build()
//        );
//        System.out.println("  Antoni <antoni@wp.pl>  ".trim());
        System.out.println("  <example-mail@wp.pl>".trim()
                .matches("^(([^<>\\[\\]@]+<(\\w+[+.-]{1}){0,}\\w+@(\\w+[.-]{1}){0,}\\w+\\.\\w{2,}>)|((\\w+[+.-]{1}){0,}\\w+@(\\w+[.-]{1}){0,}\\w+\\.\\w{2,}))$"));
        
//        System.out.println("jarek@wp.pl brozka@wp.pl".matches("^(([(\\w+[+.-]){0,2}\\w+][^@])@(\\w+[.-]{1}){0,2}\\w+\\.\\w{2,4};? ?)+$"));
        
//        String regEmail = ^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

//        System.out.println( LocalDateTime.of(2018, 5, 20, 8, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() );

        EmailDaoImpl e = new EmailDaoImpl();
        System.out.println(e.count(16l, "RECEIVED"));
        
    }
}
