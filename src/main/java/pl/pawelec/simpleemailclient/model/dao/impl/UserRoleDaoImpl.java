/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.pawelec.simpleemailclient.configuration.HibernateUtil;
import pl.pawelec.simpleemailclient.model.UserRole;
import pl.pawelec.simpleemailclient.model.dao.UserRoleDao;
import pl.pawelec.simpleemailclient.qualifier.UserRoleDaoQualifier;

/**
 *
 * @author mirek
 */
@UserRoleDaoQualifier
public class UserRoleDaoImpl implements UserRoleDao{
    private Session session = null;
    private Transaction transaction = null;

    public UserRole create(UserRole entity) {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();
        try{
            transaction.begin();
            session.persist(entity);
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

    public boolean update(UserRole entity) {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();
        try{
            transaction.begin();
            session.update(entity);
            transaction.commit();
        }catch(Exception e){
            if(transaction!=null)
                transaction.rollback();
            System.err.println(e.getMessage());
            return false;
        }finally{
            if(session!=null)
                session.close();
        }
        return true;
    }

    public boolean delete(UserRole entity) {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.getTransaction();
        try{
            transaction.begin();
            session.delete(entity);
            transaction.commit();
        }catch(Exception e){
            if(transaction!=null)
                transaction.rollback();
            System.err.println(e.getMessage());
            return false;
        }finally{
            if(session!=null)
                session.close();
        }
        return true;
    }

    public UserRole get(Serializable id) {
        UserRole userRole = null;
        String sql = "SELECT ur FROM UserRole ur WHERE ur.id = :id";
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            userRole = (UserRole) session.createQuery(sql)
                    .setParameter("id", id)
                    .uniqueResult();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return userRole;
    }

    public List<UserRole> getAll() {
        List<UserRole> userRoles = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            userRoles = session.createQuery("FROM UserRole").list();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return userRoles;
    }

    public UserRole getByLogin(String login) {
        UserRole userRole = null;
        String sql = "SELECT ur FROM UserRole ur WHERE ur.user.login = :login";
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            userRole = (UserRole) session.createQuery(sql)
                    .setParameter("login", login)
                    .uniqueResult();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return userRole;
    }
    
}
