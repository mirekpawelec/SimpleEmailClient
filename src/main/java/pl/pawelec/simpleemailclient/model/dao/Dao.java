/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.dao;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mirek
 */
public interface Dao<T extends Object> {
    T create(T entity);
    boolean update(T entity);
    boolean delete(T entity);
    T get(Serializable id);
    List<T> getAll();
}
