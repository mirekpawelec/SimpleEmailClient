/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.exception;

/**
 *
 * @author mirek
 */
public class EmailNoSentException extends RuntimeException{
    private String info;
    
    public EmailNoSentException(String message) {
        super(message);
    }

    public String getInfo() {
        return info;
    }
    
}
