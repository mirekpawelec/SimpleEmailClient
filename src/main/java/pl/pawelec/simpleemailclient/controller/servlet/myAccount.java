/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.controller.servlet;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.qualifier.UserServiceQualifier;
import pl.pawelec.simpleemailclient.service.UserService;

/**
 *
 * @author mirek
 */
@WebServlet(name = "myProfile", urlPatterns = "/app/myAccount")
public class myAccount extends HttpServlet{
    private static final String SUCCESS = "Dane zostały zmodyfikowane.";
    private static final String ERROR = "Twoje dane nie zostały zmienione.";
    @Inject @UserServiceQualifier
    private UserService userService;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/myAccount.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String password = req.getParameter("password");
        String repeatPassword = req.getParameter("repeatPassword");
        if((fullName!=null && !fullName.isEmpty())
                && (password!=null && !password.isEmpty() && password.equals(repeatPassword))){ 
            User user = (User) req.getSession().getAttribute("user");
            user = userService.get(user.getId());
            user.setFullName(fullName);
            user.setPassword(password);
            userService.update(user);
            req.getSession().setAttribute("user", user);
            req.setAttribute("success", SUCCESS);
        }else{
            req.setAttribute("error", ERROR);
        }
        doGet(req, resp);
    }
    
}
