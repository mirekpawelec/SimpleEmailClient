/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import pl.pawelec.simpleemailclient.model.Contact;
import pl.pawelec.simpleemailclient.model.Email;
import pl.pawelec.simpleemailclient.model.EmailInternetAddress;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.dao.ContactDao;
import pl.pawelec.simpleemailclient.model.enum_.EmailSeparator;
import pl.pawelec.simpleemailclient.model.enum_.RecipientType;
import pl.pawelec.simpleemailclient.qualifier.ContactDaoQualifier;
import pl.pawelec.simpleemailclient.qualifier.ContactServiceQualifier;
import pl.pawelec.simpleemailclient.service.ContactService;

/**
 *
 * @author mirek
 */
@ContactServiceQualifier
public class ContactServiceImpl implements ContactService{
    @Inject @ContactDaoQualifier
    private ContactDao contactDao;

    public Contact create(Contact entity) {
        return contactDao.create(entity);
    }

    public boolean update(Contact entity) {
        return contactDao.update(entity);
    }

    public boolean delete(Contact entity) {
        return contactDao.delete(entity);
    }

    public Contact get(Serializable id) {
        return contactDao.get(id);
    }
    
    public List<Contact> getAll() {
        return contactDao.getAll();
    }
    
    public List<Contact> getAllByUser(Long userId){
        return contactDao.getAllByUser(userId);
    }

    public Contact get(Long userId, String name, String emailAddress){
        return contactDao.get(userId, name, emailAddress);
    }
}
