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
import pl.pawelec.simpleemailclient.model.EmailAccount;
import pl.pawelec.simpleemailclient.model.dao.EmailAccountDao;
import pl.pawelec.simpleemailclient.qualifier.EmailAccountDaoQualifier;

/**
 *
 * @author mirek
 */
@EmailAccountDaoQualifier
public class EmailAccountDaoImpl implements EmailAccountDao{
    private static final String NO_FOUND_EMAIL_ACCOUNT = "The email account (id: %d) has not been found!";
    private static final String NO_FOUND_EMAIL_ACCOUNTS = "No found any email accounts!";
    private Session session = null;
    private Transaction transaction = null;
    
    public EmailAccount create(EmailAccount entity) {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();
        try{
            transaction.begin();
            session.save(entity);
            transaction.commit();
        }catch(Exception e){
            if(transaction!=null)
                transaction.rollback();
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return entity;
    }

    public boolean update(EmailAccount entity) {
        Boolean result = false;
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();
        try{
            transaction.begin();
            session.update(entity);
            transaction.commit();
            result = true;
        }catch(Exception e){
            if(transaction!=null)
                transaction.rollback();
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return result;
    }

    public boolean delete(EmailAccount entity) {
        Boolean result = false;
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();
        try{
            transaction.begin();
            session.delete(entity);
            transaction.commit();
            result = true;
        }catch(Exception e){
            if(transaction!=null)
                transaction.rollback();
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return result;
    }

    public EmailAccount get(Serializable id) throws NoResultException{
        EmailAccount emailAccount = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            emailAccount = session.get(EmailAccount.class, id);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        if(emailAccount==null)
            throw new NoResultException(String.format(NO_FOUND_EMAIL_ACCOUNT, id));
        return emailAccount;
    }

    public List<EmailAccount> getAll() throws NoResultException{
        List<EmailAccount> emailAccountsList = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            emailAccountsList = session.createQuery("FROM UserEmailAccount").list();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        if(emailAccountsList.size()==0)
            throw new NoResultException(NO_FOUND_EMAIL_ACCOUNTS);
        return emailAccountsList;
    }
    
}
