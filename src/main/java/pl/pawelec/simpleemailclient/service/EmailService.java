/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service;

import java.util.List;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import pl.pawelec.simpleemailclient.model.Email;
import pl.pawelec.simpleemailclient.model.User;

/**
 *
 * @author mirek
 */
public interface EmailService extends Service<Email>{
    boolean send(Email mail);
    String changeFlag(Long id);
    void changeReadStatus(Long id, String status);
    List<Email> getByFolder(String folderName, Long userId, Integer page, Integer limit);
    Boolean changeStatus(Long id);
    void checkEmailBox(User user);
    List<Email> getAllByUser(Long userId);
    List<Object[]> deletePermanently(List<Object[]> list);
    Long count(Long userId, String folderName);
}
