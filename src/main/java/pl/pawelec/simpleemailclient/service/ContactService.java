/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service;

import java.util.List;
import pl.pawelec.simpleemailclient.model.Contact;

/**
 *
 * @author mirek
 */
public interface ContactService extends Service<Contact>{
    List<Contact> getAllByUser(Long userId);
    Contact get(Long userId, String name, String emailAddress);
}
