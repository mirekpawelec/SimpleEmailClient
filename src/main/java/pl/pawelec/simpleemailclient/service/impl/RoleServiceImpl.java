/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service.impl;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import pl.pawelec.simpleemailclient.model.Role;
import pl.pawelec.simpleemailclient.model.dao.RoleDao;
import pl.pawelec.simpleemailclient.qualifier.RoleDaoQualifier;
import pl.pawelec.simpleemailclient.qualifier.RoleServiceQualifier;
import pl.pawelec.simpleemailclient.service.RoleService;


/**
 *
 * @author mirek
 */
@RoleServiceQualifier
public class RoleServiceImpl implements RoleService{
    @Inject @RoleDaoQualifier
    private RoleDao roleDao;
    
    public Role create(Role entity) {
        return roleDao.create(entity);
    }

    public boolean update(Role entity) {
        return roleDao.update(entity);
    }

    public boolean delete(Role entity) {
        return roleDao.delete(entity);
    }

    public Role get(Serializable id) {
        return roleDao.get(id);
    }

    public List<Role> getAll() {
        return roleDao.getAll();
    }

    public Role getByName(String roleName) {
        return roleDao.getByName(roleName);
    }
    
}
