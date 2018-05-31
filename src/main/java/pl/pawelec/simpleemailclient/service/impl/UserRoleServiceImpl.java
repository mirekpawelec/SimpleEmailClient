/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service.impl;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import pl.pawelec.simpleemailclient.model.UserRole;
import pl.pawelec.simpleemailclient.model.dao.UserRoleDao;
import pl.pawelec.simpleemailclient.qualifier.UserRoleDaoQualifier;
import pl.pawelec.simpleemailclient.qualifier.UserRoleServiceQualifier;
import pl.pawelec.simpleemailclient.service.UserRoleService;


/**
 *
 * @author mirek
 */
@UserRoleServiceQualifier
public class UserRoleServiceImpl implements UserRoleService{
    @Inject @UserRoleDaoQualifier
    private UserRoleDao userRoleDao;

    public UserRole create(UserRole entity) {
        return userRoleDao.create(entity);
    }

    public boolean update(UserRole entity) {
        return userRoleDao.update(entity);
    }

    public boolean delete(UserRole entity) {
        return userRoleDao.delete(entity);
    }

    public UserRole get(Serializable id) {
        return userRoleDao.get(id);
    }

    public List<UserRole> getAll() {
        return userRoleDao.getAll();
    }

    public UserRole getByLogin(String login) {
        return userRoleDao.getByLogin(login);
    }
}
