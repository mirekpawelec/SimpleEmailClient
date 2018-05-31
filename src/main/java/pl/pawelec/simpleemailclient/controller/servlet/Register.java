/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.controller.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.resteasy.util.Base64;
import pl.pawelec.simpleemailclient.model.EmailAccount;
import pl.pawelec.simpleemailclient.model.RedirectAttribute;
import pl.pawelec.simpleemailclient.model.Role;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.UserRole;
import pl.pawelec.simpleemailclient.qualifier.EmailAccountServiceQualifier;
import pl.pawelec.simpleemailclient.qualifier.RoleServiceQualifier;
import pl.pawelec.simpleemailclient.qualifier.UserRoleServiceQualifier;
import pl.pawelec.simpleemailclient.qualifier.UserServiceQualifier;
import pl.pawelec.simpleemailclient.service.EmailAccountService;
import pl.pawelec.simpleemailclient.service.RoleService;
import pl.pawelec.simpleemailclient.service.UserRoleService;
import pl.pawelec.simpleemailclient.service.UserService;
import pl.pawelec.simpleemailclient.utils.Encryption;

/**
 *
 * @author mirek
 */
@WebServlet(name = "register", urlPatterns = "/app/register")
public class Register extends HttpServlet{
    private static final String VALIDATION_ERROR = "Konto nie zostało utworzone."; //"The account hasn't been created!";
    private static final String USER_EXIST_ERROR = "The given login is used!";
    private static final String USER_CREATING_ERROR = "It has failed of creating the user!";
    private static final String USER_ROLE = "USER"; 
    private static final String DESCRIPTION_USER_ROLE = "Użytkownik"; 
    @Inject @UserServiceQualifier
    private UserService userService;
    @Inject @RoleServiceQualifier
    private RoleService roleService;
    @Inject @UserRoleServiceQualifier
    private UserRoleService userRoleService;
    @Inject @EmailAccountServiceQualifier
    private EmailAccountService emailAccountService;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = null;
        UserRole userRole = null; 
        EmailAccount emailAccount = null;
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String emailAddress = req.getParameter("emailAddress");
        String emailPassword = req.getParameter("emailPassword");
        String emailRepeatPassword = req.getParameter("emailRepeatPassword");
        String pop3Address = req.getParameter("pop3Address");
        String pop3Port = req.getParameter("pop3Port");
        String smtpAddress = req.getParameter("smtpAddress");
        String smtpPort = req.getParameter("smtpPort");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String repeatPassword = req.getParameter("repeatPassword");
        
        Set<Object[]> infoValidErrors = new HashSet<>();
        if(firstName==null || "".equals(firstName))
            infoValidErrors.add(new Object[]{"Imię","dodaj imie."});
        if(lastName==null || "".equals(lastName))
            infoValidErrors.add(new Object[]{"Nazwisko","dodaj nazwisko."});
        if(emailAddress==null || "".equals(emailAddress) || !checkPatternEmail(emailAddress))
            infoValidErrors.add(new Object[]{"Adres e-mail","brak lub wpisana wartość nie jest adresem e-mail."});
        if(emailPassword==null || "".equals(emailPassword) || !emailPassword.equals(emailRepeatPassword))
            infoValidErrors.add(new Object[]{"Hasło konta e-mail","brak, lub wpisane wartości nie są identyczne."});
        if(pop3Address==null  || "".equals(pop3Address) || pop3Port==null || "".equals(pop3Port))
            infoValidErrors.add(new Object[]{"Serwer POP3","nazwa i numer serwera POP3 sa wymagane."});
        if(smtpAddress==null || "".equals(smtpAddress) || smtpPort==null || "".equals(smtpPort))
            infoValidErrors.add(new Object[]{"Serwer SMTP","nazwa i numer serwera SMTP są wymagane."});
        if(login==null || "".equals(login) || !checkPatternLogin(login))
            infoValidErrors.add(new Object[]{"Login","brak lub użyłeś nieodpowiednich znaków (tylko alfanumeryczne)."});
        if(password==null || "".equals(password) || !password.equals(repeatPassword))
            infoValidErrors.add(new Object[]{"Hasło konta SEC","brak, lub hasła nie są identyczne."});
        if( ! checkPatternPassword(password))
            infoValidErrors.add(new Object[]{"Hasło konta SEC","nie spełnia wymagań (użyj min. 8 znaków, dodaj cyfrę, dużą i małą literę)."});
        try{ 
            userService.getByLogin(login); 
            infoValidErrors.add(new Object[]{"Login","podany login jest już używany."});
        }catch(NoResultException nr){}
        
        if(infoValidErrors.isEmpty()){
            user = userService.create(new User(login, password, firstName+" "+lastName));
            if(user!=null){
                userRole = new UserRole();
                userRole.setLogin(user);
                if( ! Optional.ofNullable(roleService.getByName(USER_ROLE)).isPresent()){
                    roleService.create(new Role(USER_ROLE, DESCRIPTION_USER_ROLE));
                }
                userRole.setRole(roleService.getByName(USER_ROLE));
                userRoleService.create(userRole);
                emailAccount = new EmailAccount(user, firstName, lastName, emailAddress, emailPassword, 
                        smtpAddress, Integer.valueOf(smtpPort), pop3Address, Integer.valueOf(pop3Port));
                emailAccountService.create(emailAccount);
                req.getSession().setAttribute("success", new RedirectAttribute(user.getFullName()));
                resp.sendRedirect(req.getContextPath() + "/app/index");
                return;
            }else{
                req.setAttribute("error", USER_CREATING_ERROR);
            };

        }else{
            req.setAttribute("error", VALIDATION_ERROR);  
            req.setAttribute("errorMap", infoValidErrors);  
            infoValidErrors.forEach(e->System.out.println(Arrays.toString(e)));
        }
        doGet(req, resp);
    }
    
    private boolean checkPatternEmail(String value){
        return value.matches("^((\\w+[+-.]\\w+)|(\\w+))@(\\w+[-.]){0,2}\\w+\\.\\w{2,}$");
    }
    
    private boolean checkPatternLogin(String value){
        return value.matches("^\\w+$");
    }
    
    private boolean checkPatternPassword(String value){
        if(value.matches("^.*[a-z]+.*$") && value.matches("^.*[A-Z]+.*$") && value.matches("^.*[0-9]+.*$") && value.matches("^.{8,}$")){
            return true;
        }
        return false;
    }
}



