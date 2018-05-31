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
public enum EmailSeparator {
    TEXT(","),
    REG_EX(", ?");
    
    private String separator;

    private EmailSeparator(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }
}
