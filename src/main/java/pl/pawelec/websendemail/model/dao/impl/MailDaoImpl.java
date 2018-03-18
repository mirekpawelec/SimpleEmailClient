/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.model.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import pl.pawelec.websendemail.model.Mail;
import pl.pawelec.websendemail.model.dao.MailDao;

/**
 *
 * @author mirek
 */
public class MailDaoImpl implements MailDao{
    private static HashMap<Integer, Mail> emails = new HashMap<Integer, Mail>();
    private static AtomicInteger counterEmails = new AtomicInteger(0);
    private static MailDaoImpl instance;
    
    private MailDaoImpl() {
        save(new Mail("odbieracz@wp.pl","","test","wiadomość testowa","RECEIVED"));
    }

    public static MailDaoImpl getInstance(){
        if(instance==null){
            instance = new MailDaoImpl();
        }
        return instance;
    }
    
    @Override
    public Mail get(Integer id) {
        return emails.get(id);
    }

    @Override
    public List<Mail> getAll() {
        return new ArrayList<Mail>(emails.values());
    }

    @Override
    public Integer save(Mail mail) {
        int id = incrementCounterEmails();
        mail.setId(Long.valueOf(id));
        emails.put(id, mail);
        return id;
    }

    @Override
    public boolean update(Integer id, Mail mail) {
        return emails.replace(id, get(id), mail);
    }

    @Override
    public boolean delete(Mail mail) {
        return emails.remove(mail.getId(), mail);
    }
    
    private Integer incrementCounterEmails(){
        return counterEmails.incrementAndGet();
    }
}
