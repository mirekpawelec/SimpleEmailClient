/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import pl.pawelec.simpleemailclient.model.EmailInternetAddress;
import pl.pawelec.simpleemailclient.model.dao.EmailInternetAddressDao;
import pl.pawelec.simpleemailclient.qualifier.EmailInternetAddressDaoQualifier;
import pl.pawelec.simpleemailclient.qualifier.EmailInternetAddressServiceQualifier;
import pl.pawelec.simpleemailclient.service.EmailInternetAddressService;

/**
 *
 * @author mirek
 */
@EmailInternetAddressServiceQualifier
public class EmailInternetAddressServiceImpl implements EmailInternetAddressService{
    @Inject @EmailInternetAddressDaoQualifier
    private EmailInternetAddressDao emailInternetAddressDao; 
    
    public EmailInternetAddress create(EmailInternetAddress entity) {
        return emailInternetAddressDao.create(entity);
    }

    public boolean update(EmailInternetAddress entity) {
        return emailInternetAddressDao.update(entity);
    }

    public boolean delete(EmailInternetAddress entity) {
        return emailInternetAddressDao.delete(entity);
    }

    public EmailInternetAddress get(Serializable id) {
        return emailInternetAddressDao.get(id);
    }

    public List<EmailInternetAddress> getAll() {
        return emailInternetAddressDao.getAll();
    }

    public List<EmailInternetAddress> getListByEmailId(Long emailId) {
        return emailInternetAddressDao.getByEmailId(emailId);
    }

    public Set<EmailInternetAddress> getSetByEmailId(Long emailId) {
        return emailInternetAddressDao.getByEmailId(emailId).stream().collect(Collectors.toSet());
    }
    
}
