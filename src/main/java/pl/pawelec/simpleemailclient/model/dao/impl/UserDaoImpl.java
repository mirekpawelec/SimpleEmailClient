/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.dao.impl;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.pawelec.simpleemailclient.configuration.HibernateUtil;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.dao.UserDao;
import pl.pawelec.simpleemailclient.qualifier.UserDaoQualifier;
import pl.pawelec.simpleemailclient.utils.Encryption;

/**
 *
 * @author mirek
 */
@UserDaoQualifier
public class UserDaoImpl implements UserDao{
    private Session session = null;
    private Transaction transaction = null;
    
    public User create(User entity) {
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

    public boolean update(User entity) {
        boolean result = false;
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

    public boolean delete(User entity) {
        boolean result = false;
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

    public User get(Serializable id) {
        User user = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            user = session.get(User.class, id);
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return user;
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            users = session.getNamedQuery("User.findAll").list();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return users;
    }

    @Override
    public User getBySessionId(String sessionId) {
        User user = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            user = (User) session.getNamedQuery("User.findBySessionId").
                    setParameter("sessionId", sessionId)
                    .uniqueResult();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return user;
    }

    @Override
    public User getByLogin(String login) {
        User user = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            user = (User) session.getNamedQuery("User.findByLogin")
                    .setParameter("login", login)
                    .uniqueResult();
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        if(user==null)
                throw new NoResultException();
        return user;
    }
}
