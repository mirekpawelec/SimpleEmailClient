/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.dao;

import java.util.List;
import java.util.Set;
import pl.pawelec.simpleemailclient.model.EmailInternetAddress;

/**
 *
 * @author mirek
 */
public interface EmailInternetAddressDao extends Dao<EmailInternetAddress>{
    List<EmailInternetAddress> getByEmailId(Long emailId);
}
