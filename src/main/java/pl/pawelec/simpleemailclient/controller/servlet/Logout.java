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
import javax.servlet.http.Cookie;
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
@WebServlet(name = "logout", urlPatterns = "/app/logout")
public class Logout extends HttpServlet{
    @Inject @UserServiceQualifier
    private UserService userService;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        user.setSessionId("");
        user.setExpirationDate(0l); 
        userService.update(user);
        
        for(Cookie cookie : req.getCookies()){
            if(cookie.getName().equals("sessionId")){
                Cookie sessionId = new Cookie(cookie.getName(), "");
                sessionId.setMaxAge(0);
                sessionId.setPath(req.getContextPath()); 
                resp.addCookie(sessionId);
            }
        }
            
        req.getSession().setAttribute("user", null);
        req.logout();
        resp.sendRedirect(req.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
}
