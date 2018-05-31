/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.pawelec.simpleemailclient.configuration.HibernateUtil;
import pl.pawelec.simpleemailclient.exception.EmailCanNotDeleteException;
import pl.pawelec.simpleemailclient.exception.EmailNoFoundException;
import pl.pawelec.simpleemailclient.model.Email;
import pl.pawelec.simpleemailclient.model.dao.EmailDao;
import pl.pawelec.simpleemailclient.qualifier.EmailDaoQualifier;

/**
 *
 * @author mirek
 */
@EmailDaoQualifier
public class EmailDaoImpl implements EmailDao{  
    private static final String NO_FOUND_EMAIL = "The email (id: %d) has not been found!";
    private static final String NO_FOUND_EMAILS = "No found any emails!";
    private static final String EMAIL_NO_DELETE = "The email (id: %d) cannot delete.";
    private Session session = null;
    private Transaction transaction = null;

    public Email create(Email entity) {
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

    public boolean update(Email entity) {
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

    public boolean delete(Email entity) throws EmailCanNotDeleteException{
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
            throw new EmailCanNotDeleteException(String.format(EMAIL_NO_DELETE, entity.getId()));
        }finally{
            if(session!=null) 
                session.close();
        }
        return result;
    }

    public Email get(Serializable id) throws EmailNoFoundException{
        Email email = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            email = session.get(Email.class, id);        
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null) 
                session.close();
        }
        if(email==null)
            throw new EmailNoFoundException(String.format(NO_FOUND_EMAIL, id));
        return email;
    }

    public List<Email> getAll() throws EmailNoFoundException{
        List<Email> emailsList = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            emailsList = session.createQuery("FROM Email").list();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null) 
                session.close();
        }
        if(emailsList.size()==0)
            throw new EmailNoFoundException(NO_FOUND_EMAILS);
        return emailsList;
    }

    public List<Email> getByFolder(String folderName, Long userId, Integer page, Integer limit) throws EmailNoFoundException{
        List<Email> emailsList = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().openSession();
        Integer offset = (page*limit)-limit;
        Integer maxSize = offset+limit;
        try{
            String sql = "FROM Email e "
                       + "WHERE e.folder = :folderName "
                       + "AND e.user.id = :userId "
                       + "order by e.createDate desc";
            emailsList = session.createQuery(sql)
                    .setParameter("folderName", folderName)
                    .setParameter("userId", userId)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .list();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null) 
                session.close();
        }
        if(emailsList.isEmpty()){
            throw new EmailNoFoundException(NO_FOUND_EMAILS);
        }
        emailsList.sort(Comparator.comparing(Email::getCreateDate));
        return emailsList;
    }
    
    public Long count(Long userId, String folderName){
        Long count = 0l;
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            String sql = "SELECT count(*) FROM Email e WHERE e.user.id = :userId AND e.folder = :folderName";
            count = (Long) session.createQuery(sql)
                    .setParameter("userId", userId)
                    .setParameter("folderName", folderName)
                    .uniqueResult();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return count;
    }
    
    public List<Email> getAllByUser(Long userId) {
        session = HibernateUtil.getSessionFactory().openSession();
        List <Email> emailsList = new ArrayList<>();
        try{
            emailsList = session.createQuery("FROM Email e WHERE e.user.id = :id")
                    .setParameter("id", userId)
                    .list();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        if(emailsList.size()==0)
            throw new EmailNoFoundException(NO_FOUND_EMAILS);
        return emailsList;
    }
}
