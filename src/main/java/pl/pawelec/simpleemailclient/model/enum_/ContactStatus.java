/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.enum_;

/**
 *
 * @author mirek
 */
public enum ContactStatus {
    OK,
    OFF,
    SPAM,
    BLACK_LIST;
    
    public String getName(){
        return this.name();
    }
}
