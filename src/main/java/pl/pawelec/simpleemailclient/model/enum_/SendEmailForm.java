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
public enum SendEmailForm {
    ID("id"),
    SENDER("sender"), 
    RECIPIENT("recipient"), 
    ADDITIONAL_RECIPIENT("additionalRecipient"), 
    TITLE("title"),
    MESSAGE("message"), 
    ATTACHMENT_NAME("attachmentName"),
    ATTACHMENTS("attachments");
    
    private String name;

    private SendEmailForm(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }    
}
