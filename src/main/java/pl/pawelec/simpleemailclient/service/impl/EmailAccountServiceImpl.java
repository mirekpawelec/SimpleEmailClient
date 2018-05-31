/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service.impl;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import pl.pawelec.simpleemailclient.model.EmailAccount;
import pl.pawelec.simpleemailclient.model.dao.EmailAccountDao;
import pl.pawelec.simpleemailclient.qualifier.EmailAccountDaoQualifier;
import pl.pawelec.simpleemailclient.qualifier.EmailAccountServiceQualifier;
import pl.pawelec.simpleemailclient.service.EmailAccountService;

/**
 *
 * @author mirek
 */
@EmailAccountServiceQualifier
public class EmailAccountServiceImpl implements EmailAccountService{
    @Inject @EmailAccountDaoQualifier
    private EmailAccountDao emailAccountDao;
    
    @Override
    public EmailAccount create(EmailAccount entity) {
        return emailAccountDao.create(entity);
    }

    @Override
    public boolean update(EmailAccount entity) {
        return emailAccountDao.update(entity);
    }

    @Override
    public boolean delete(EmailAccount entity) {
        return emailAccountDao.delete(entity);
    }

    @Override
    public EmailAccount get(Serializable id) {
        return emailAccountDao.get(id);
    }

    @Override
    public List<EmailAccount> getAll() {
        return emailAccountDao.getAll();
    }
    
}
