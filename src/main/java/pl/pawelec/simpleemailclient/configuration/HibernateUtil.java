/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.configuration;

import java.util.HashMap;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import pl.pawelec.simpleemailclient.model.Contact;
import pl.pawelec.simpleemailclient.model.Email;
import pl.pawelec.simpleemailclient.model.Role;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.EmailAccount;
import pl.pawelec.simpleemailclient.model.EmailInternetAddress;
import pl.pawelec.simpleemailclient.model.UserRole;

/**
 *
 * @author mirek
 */
public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;
    
    public static SessionFactory getSessionFactory(){
        if(sessionFactory==null)
            buildSessionFactory();
        return sessionFactory;
    }

    public static void buildSessionFactory() {
        try{
            Map<String, String> settings = new HashMap<>();
            settings.put(Environment.USER, "root");
            settings.put(Environment.PASS, "root");
            settings.put(Environment.URL, "jdbc:mysql://localhost:3306/smc");
            settings.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
            settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
//            settings.put(Environment.SHOW_SQL, "true");
//            settings.put(Environment.FORMAT_SQL, "true");
            
            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
            registryBuilder.applySettings(settings);
            registry = registryBuilder.build();
            
            MetadataSources metadataSources = new MetadataSources(registry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Role.class)
                .addAnnotatedClass(UserRole.class)
                .addAnnotatedClass(Email.class)
                .addAnnotatedClass(EmailAccount.class)
                .addAnnotatedClass(Contact.class)
                .addAnnotatedClass(EmailInternetAddress.class);
            Metadata metadata = metadataSources.getMetadataBuilder().build();
            
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        }catch(Exception e){
            if(registry!=null)
                StandardServiceRegistryBuilder.destroy(registry);
            e.printStackTrace();
        }
    }
    
    public static void shutdown(){
        if(registry!=null)
            StandardServiceRegistryBuilder.destroy(registry);
    }
}
