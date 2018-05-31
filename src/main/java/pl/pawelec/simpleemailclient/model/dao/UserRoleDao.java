/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.dao;

import pl.pawelec.simpleemailclient.model.UserRole;



/**
 *
 * @author mirek
 */
public interface UserRoleDao extends Dao<UserRole>{ 
    UserRole getByLogin(String login);
}
