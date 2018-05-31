/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.filter;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import pl.pawelec.simpleemailclient.model.RedirectAttribute;

/**
 *
 * @author mirek
 */
@WebFilter(urlPatterns = "/*", asyncSupported = true)
public class HandlingRedirectAttribute implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        if(session!=null){
            synchronized(session){
                Enumeration<String> sessionAttributes = session.getAttributeNames();
                while(sessionAttributes.hasMoreElements()){
                    String key = (String) sessionAttributes.nextElement();
                    Object value = session.getAttribute(key);
                    if(value instanceof RedirectAttribute){
                        if(((RedirectAttribute) value).toRemove())
                            session.removeAttribute(key);
                    }
                };
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
    
}
