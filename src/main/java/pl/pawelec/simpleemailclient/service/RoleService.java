/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service;

import pl.pawelec.simpleemailclient.model.Role;


/**
 *
 * @author mirek
 */
public interface RoleService extends Service<Role>{
    Role getByName(String roleName);
}
