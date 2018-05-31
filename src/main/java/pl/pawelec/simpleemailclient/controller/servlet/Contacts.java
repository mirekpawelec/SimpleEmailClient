/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.controller.servlet;

import java.io.IOException;
import java.util.Arrays;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.pawelec.simpleemailclient.model.Contact;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.enum_.ContactStatus;
import pl.pawelec.simpleemailclient.qualifier.ContactServiceQualifier;
import pl.pawelec.simpleemailclient.service.ContactService;

/**
 *
 * @author mirek
 */
@WebServlet(name="contacts", urlPatterns = "/app/contacts")
public class Contacts extends HttpServlet{
    private static final String SUCCESS_CREATE = "Kontakt został dodany.";
    private static final String SUCCESS_UPDATE = "Kontakt został zmodyfikowany.";
    private static final String SUCCESS_DELETE = "Kontakt \"%s\" został usunięty.";
    private static final String CREATE_UPDATE_ERROR = "Wystąpił błąd. Nie udało się dodać/zmodyfikować kontaktu.";
    private static final String VALIDATION_ERROR = "Nieprawidłowy format danych. Nie udało się dodać/zmodyfikować kontaktu.";
    private static final String DELETE_ERROR = "Nie udało się usunąć kontaktu \"%s\".";
    @Inject @ContactServiceQualifier
    private ContactService contactService;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        req.setAttribute("contacts", contactService.getAllByUser(user.getId()));
        req.setAttribute("contactStatuses", Arrays.asList(ContactStatus.values()));
        req.getRequestDispatcher("/WEB-INF/views/contacts.jsp").forward(req, resp); 
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Contact contact = null;
        User user = (User) req.getSession().getAttribute("user");
        Boolean delete = Boolean.valueOf(req.getParameter("delete"));
        String contactId = req.getParameter("contactId");
        String name = req.getParameter("name");
        String emailAddress = req.getParameter("emailAddress");
        String status = req.getParameter("status");
//        System.out.println("contactId="+contactId+", name="+name+", emailAddress="+emailAddress+", status="+status+", delete="+delete);
        if(delete){
            contact = contactService.get(Long.valueOf(contactId));
            if(contactService.delete(contact)){
                req.setAttribute("success", String.format(SUCCESS_DELETE, contact.getName()+" "+contact.getEmailAddress()));
            }else{
                req.setAttribute("error", String.format(DELETE_ERROR, contact.getName()+" "+contact.getEmailAddress()));
            }
        }else{             
            if( (name.isEmpty() || (!name.isEmpty() && name.matches("^([^<>\\[\\]@] *)*$")))
                    && !emailAddress.isEmpty() && emailAddress.matches("^(\\w+[+.-]{1}){0,}\\w+@(\\w+[.-]{1}){0,}\\w+\\.\\w{2,}$") 
                        && !status.isEmpty()){
                try{
                    if(contactId.isEmpty()){ 
                        contact = new Contact(user, name, emailAddress);
                        contact.setStatus(status);
                        if(contactService.create(contact).isNew()==true)
                            throw new Exception();
                        req.setAttribute("success", SUCCESS_CREATE);
                    }else{
                        contact = contactService.get(Long.valueOf(contactId));
                        contact.setName(name);
                        contact.setEmailAddress(emailAddress);
                        contact.setStatus(status);
                        if( !contactService.update(contact))
                            throw new Exception();
                        req.setAttribute("success", SUCCESS_UPDATE);
                    }
                }catch(Exception e){
                    req.setAttribute("error", CREATE_UPDATE_ERROR);
                }
            }else{
                req.setAttribute("error", VALIDATION_ERROR);
            }
        }
        doGet(req, resp);
    }
    
}
