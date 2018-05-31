/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.dao;

import java.util.List;
import pl.pawelec.simpleemailclient.model.Email;

/**
 *
 * @author mirek
 */
public interface EmailDao extends Dao<Email>{
    List<Email> getByFolder(String folderName, Long userId, Integer page, Integer limit);
    List<Email> getAllByUser(Long userId);
    Long count(Long userId, String folderName);
}
