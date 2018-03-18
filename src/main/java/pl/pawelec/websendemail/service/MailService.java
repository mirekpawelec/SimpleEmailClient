/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.service;

import java.util.List;
import pl.pawelec.websendemail.model.Mail;

/**
 *
 * @author mirek
 */
public interface MailService {
    Mail get(Integer id);
    List<Mail> getAll();
    Integer save(Mail mail);
    boolean update(Integer id, Mail mail);
    boolean delete(Mail mail);
    boolean sendEmail(Mail mail);
}
