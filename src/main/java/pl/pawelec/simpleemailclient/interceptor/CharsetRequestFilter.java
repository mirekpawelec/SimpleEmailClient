/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.interceptor;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

/**
 *
 * @author mirek
 */
@Provider
public class CharsetRequestFilter implements ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(InputPart.DEFAULT_CHARSET_PROPERTY, "UTF-8");
    }
    
}
