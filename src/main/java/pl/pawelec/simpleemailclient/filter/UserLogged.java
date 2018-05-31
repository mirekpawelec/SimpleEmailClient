/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.filter;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.qualifier.UserServiceQualifier;
import pl.pawelec.simpleemailclient.service.UserService;

/**
 *
 * @author mirek
 */
@WebFilter(urlPatterns = "/app/*", asyncSupported = true)
public class UserLogged implements Filter{
    private static final Long EXPIRATION_DATE = 3600L;
    @Inject @UserServiceQualifier
    private UserService userService;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");
        String login = req.getRemoteUser();
        if(login!=null){     
            User user = (User) req.getSession().getAttribute("user");
            if(user==null){
                user = userService.getByLogin(login);
                if(user.getSessionId()==null || user.getSessionId().isEmpty()){
                    user.setSessionId( generateRandomHexToken(64) );
                    user.setExpirationDate(LocalDateTime.now().toInstant(ZoneOffset.ofHours(1)).toEpochMilli()+EXPIRATION_DATE); 
                    user.setLastLoginDate(LocalDateTime.now()); 
                    userService.update(user);
                }
                req.getSession().setAttribute("user", user);

                Cookie sessionId = new Cookie("sessionId", user.getSessionId());
                sessionId.setMaxAge(43200);
                sessionId.setPath(req.getContextPath());
                resp.addCookie(sessionId); 
            }
        }
        chain.doFilter(request, response); 
    }

    @Override
    public void destroy() {}
    
    private static String generateRandomHexToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16); //hex encoding
    }
}
