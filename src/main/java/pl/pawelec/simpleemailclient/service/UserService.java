/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service;

import javax.servlet.http.HttpServletRequest;
import pl.pawelec.simpleemailclient.model.User;

/**
 *
 * @author mirek
 */
public interface UserService extends Service<User>{
    User getBySessionId(String sessionId);
    User getByLogin(String login);
    User getUserFromRequest(HttpServletRequest request);
    User getByEncryptedSessionId(String encryptedSessionId);
}
