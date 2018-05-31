/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.pawelec.simpleemailclient.configuration.HibernateUtil;
import pl.pawelec.simpleemailclient.model.EmailInternetAddress;
import pl.pawelec.simpleemailclient.model.dao.EmailInternetAddressDao;
import pl.pawelec.simpleemailclient.qualifier.EmailInternetAddressDaoQualifier;

/**
 *
 * @author mirek
 */
@EmailInternetAddressDaoQualifier
public class EmailInternetAddressDaoImpl implements EmailInternetAddressDao{
    private Session session = null;
    private Transaction transaction = null;
    
    public EmailInternetAddress create(EmailInternetAddress entity) {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();
        try{
            transaction.begin();
            session.save(entity);
            transaction.commit();
        }catch(Exception e){
            if(transaction!=null){
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        return entity;
    }

    public boolean update(EmailInternetAddress entity) {
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
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        return result;
    }

    public boolean delete(EmailInternetAddress entity) {
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
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        return result;
    }

    public EmailInternetAddress get(Serializable id) throws NoResultException{
        EmailInternetAddress eia = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            eia = session.get(EmailInternetAddress.class, id);
        }catch(Exception e){
            if(transaction!=null){
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        if(eia==null){
            throw new NoResultException();
        }
        return eia;
    }

    public List<EmailInternetAddress> getAll() throws NoResultException{
        List<EmailInternetAddress> eiasList = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            eiasList = session.createQuery("FROM EmailInternetAddress").list();
        }catch(Exception e){
            if(transaction!=null){
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        if(eiasList.isEmpty()){
            throw new NoResultException();
        }
        return eiasList;
    }

    public List<EmailInternetAddress> getByEmailId(Long emailId) {
        List<EmailInternetAddress> eiasList = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            eiasList = session.createQuery("FROM EmailInternetAddress WHERE email.id = :emailId")
                    .setParameter("emailId", emailId).list();
        }catch(Exception e){
            if(transaction!=null){
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }finally{
            if(session!=null){
                session.close();
            }
        }
        if(eiasList.isEmpty()){
            throw new NoResultException();
        }
        return eiasList;
    }
    
}