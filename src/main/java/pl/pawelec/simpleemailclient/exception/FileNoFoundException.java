/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.exception;

/**
 *
 * @author mirek
 */
public class FileNoFoundException extends RuntimeException{
    public FileNoFoundException(String message) {
        super(message);
    }
}
