/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.create;

import javax.mail.internet.InternetAddress;

/**
 *
 * @author mirek
 */
@FunctionalInterface
public interface CreateInternetAddress{
    InternetAddress create(String address, String name, String charset);
}
