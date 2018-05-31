/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.jboss.resteasy.util.Base64;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.dao.UserDao;
import pl.pawelec.simpleemailclient.qualifier.UserDaoQualifier;
import pl.pawelec.simpleemailclient.qualifier.UserServiceQualifier;
import pl.pawelec.simpleemailclient.service.UserService;


/**
 *
 * @author mirek
 */
@UserServiceQualifier
public class UserServiceImpl implements UserService{
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    @Inject @UserDaoQualifier
    private UserDao userDao;
            
    public User create(User entity) {
        return userDao.create(entity);
    }

    public boolean update(User entity) {
        return userDao.update(entity);
    }

    public boolean delete(User entity) {
        return userDao.delete(entity);
    }

    public User get(Serializable id) {
        return userDao.get(id);
    }

    public List<User> getAll() {
        return userDao.getAll();
    }

    public User getBySessionId(String sessionId) {
        return userDao.getBySessionId(sessionId);
    }

    public User getByLogin(String login) {
        return userDao.getByLogin(login);
    }

    @Override
    public User getUserFromRequest(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            user = this.getByEncryptedSessionId(request.getHeader(AUTHORIZATION_PROPERTY));
        }
        return user;
    }
    
    @Override
    public User getByEncryptedSessionId(String encryptedSessionId) throws RuntimeException{
        User user = null;
        try {
            user = userDao.getBySessionId(new String(Base64.decode(encryptedSessionId)));
        } catch (Exception ex) {            
            throw new RuntimeException("It's occurred an error while decode session number!");
        }
        return user;
    }

}
