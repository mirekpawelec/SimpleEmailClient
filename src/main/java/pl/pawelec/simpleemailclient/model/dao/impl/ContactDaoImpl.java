/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.pawelec.simpleemailclient.configuration.HibernateUtil;
import pl.pawelec.simpleemailclient.model.Contact;
import pl.pawelec.simpleemailclient.model.dao.ContactDao;
import pl.pawelec.simpleemailclient.qualifier.ContactDaoQualifier;

/**
 *
 * @author mirek
 */
@ContactDaoQualifier
public class ContactDaoImpl implements ContactDao{
    private Session session = null;
    private Transaction transaction = null;
    
    public Contact create(Contact entity) {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();
        try{
            transaction.begin();
            session.save(entity);
            transaction.commit();
        }catch(Exception e){
            if(transaction!=null){
                transaction.rollback();;
            }
            System.out.println("Error: "+e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        return entity;
    }

    public boolean update(Contact entity) {
        boolean result = false;
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();
        try{
            transaction.begin();
            session.update(entity);
            transaction.commit();
            result = true;
        }catch(Exception e){
            if(transaction!=null){
                transaction.rollback();;
            }
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        return result;
    }

    public boolean delete(Contact entity) {
        boolean result = false;
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();
        try{
            transaction.begin();
            session.delete(entity);
            transaction.commit();
            result = true;
        }catch(Exception e){
            if(transaction!=null){
                transaction.rollback();;
            }
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        return result;
    }

    public Contact get(Serializable id) throws NoResultException{
        Contact contact = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            contact = session.get(Contact.class, id); 
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        if(contact == null){
            throw new NoResultException();
        }
        return contact;
    }

    public List<Contact> getAll() throws NoResultException{
        List<Contact> contactsList = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            contactsList = session.createQuery("FROM Contact").list();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        if(contactsList.isEmpty()){
            throw new NoResultException();
        }
        return contactsList;
    }

    public List<Contact> getAllByUser(Long userId) throws NoResultException{
        List<Contact> contactsList = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            contactsList = session.createQuery("FROM Contact WHERE user.id = :id")
                    .setParameter("id", userId).list();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        if(contactsList.isEmpty()){
            throw new NoResultException();
        }
        return contactsList;
    }

    public Contact get(Long userId, String name, String emailAddress) throws NoResultException {
        Contact contact = null; 
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            String sql = "FROM Contact "
                       + "WHERE user.id = :userId "
                       + "AND name = :name "
                       + "AND emailAddress = :emailAddress";
            contact = (Contact) session.createQuery(sql)
                        .setParameter("userId", userId)
                        .setParameter("name", name)
                        .setParameter("emailAddress", emailAddress)
                        .uniqueResult(); 
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        if(contact==null){
            throw new NoResultException();
        }        
        return contact;
    }
    
}
