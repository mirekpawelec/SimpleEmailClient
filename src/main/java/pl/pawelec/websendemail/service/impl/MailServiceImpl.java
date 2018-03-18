/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.service.impl;

import java.util.List;
import pl.pawelec.websendemail.model.Mail;
import pl.pawelec.websendemail.model.dao.MailDao;
import pl.pawelec.websendemail.model.dao.impl.MailDaoImpl;
import pl.pawelec.websendemail.service.MailService;
import pl.pawelec.websendemail.utils.MailUtils;

/**
 *
 * @author mirek
 */
public class MailServiceImpl implements MailService{
    private static final String EMAIL_ADDRESS = "java.email@wp.pl";
    private static final String EMAIL_PASSWORD = "Java8888";
    private MailDao mailDao = MailDaoImpl.getInstance();
    
    public Mail get(Integer id) {
        return mailDao.get(id);
    }

    public List<Mail> getAll() {
        return mailDao.getAll();
    }

    public Integer save(Mail mail) {
        return mailDao.save(mail);
    }

    public boolean update(Integer id, Mail mail) {
        return mailDao.update(id, mail);
    }

    public boolean delete(Mail mail) {
        return mailDao.delete(mail);
    }

    public boolean sendEmail(Mail mail) {
        return MailUtils.send(EMAIL_ADDRESS, EMAIL_PASSWORD, mail.getRecipientEmail(), mail.getAdditionalRecipientEmail(), mail.getTitle(), mail.getMessage()); 
    }
    
    
    
}
