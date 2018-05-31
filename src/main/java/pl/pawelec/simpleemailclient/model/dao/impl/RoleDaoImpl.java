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
import pl.pawelec.simpleemailclient.model.Role;
import pl.pawelec.simpleemailclient.model.dao.RoleDao;
import pl.pawelec.simpleemailclient.qualifier.RoleDaoQualifier;


/**
 *
 * @author mirek
 */
@RoleDaoQualifier
public class RoleDaoImpl implements RoleDao{
    private Session session = null;
    private Transaction transaction = null;

    public Role create(Role entity) {
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

    public boolean update(Role entity) {
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

    public boolean delete(Role entity) {
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

    public Role get(Serializable id) {
        Role role = null;
        String sql = "SELECT r FROM Role r WHERE r.id = :id";
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            role = (Role) session.createQuery(sql).setParameter("id", id)
                    .uniqueResult();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return role;
    }

    public List<Role> getAll() {
        List<Role> roles = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            roles = session.createQuery("FROM Role").list();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return roles;
    }

    public Role getByName(String roleName) {
        Role role = null;
        String sql = "SELECT r FROM Role r WHERE r.roleName = :roleName";
        session = HibernateUtil.getSessionFactory().openSession();
        try{
            role = (Role) session.createQuery(sql).setParameter("roleName", roleName)
                    .uniqueResult();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }finally{
            if(session!=null)
                session.close();
        }
        return role;
    }
    
}
