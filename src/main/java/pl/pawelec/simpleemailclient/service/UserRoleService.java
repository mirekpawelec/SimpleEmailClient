/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service;

import pl.pawelec.simpleemailclient.model.UserRole;



/**
 *
 * @author mirek
 */
public interface UserRoleService extends Service<UserRole>{
     UserRole getByLogin(String login);
}
