/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.util.Base64;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.UserRole;
import pl.pawelec.simpleemailclient.qualifier.UserRoleServiceQualifier;
import pl.pawelec.simpleemailclient.qualifier.UserServiceQualifier;
import pl.pawelec.simpleemailclient.service.UserRoleService;
import pl.pawelec.simpleemailclient.service.UserService;

/**
 *
 * @author mirek
 */
@Provider
public class SecurityInterceptor implements ContainerRequestFilter{

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final ServerResponse ACCESS_DENIED = 
            new ServerResponse("Access denied for this resource", 401, new Headers<Object>());
    private static final ServerResponse ACCESS_FORBIDDEN = 
            new ServerResponse("Nobody can access this resources", 403, new Headers<Object>());
    private static final ServerResponse SERVER_ERROR = 
            new ServerResponse("Internal server error", 500, new Headers<Object>());
    
    @Inject @UserServiceQualifier
    private UserService userService;
    @Inject @UserRoleServiceQualifier
    private UserRoleService userRoleService;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();

        if( ! method.isAnnotationPresent(PermitAll.class)){
            if(method.isAnnotationPresent(DenyAll.class)){
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }
            
            MultivaluedMap<String, String> headers = requestContext.getHeaders();
            List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
            if(authorization==null || authorization.isEmpty()){
                requestContext.abortWith(ACCESS_DENIED);
                System.out.println("No authorization header!");
                return;
            }
            
            String authorizationSessionId = authorization.get(0);
            String session = "";
            try{
                session = new String(Base64.decode(authorizationSessionId));
            }catch(Exception e){
                requestContext.abortWith(SERVER_ERROR);
                System.out.println("It has occurred an error while decode the auth session!");
                return;
            }
            if(method.isAnnotationPresent(RolesAllowed.class)){
                RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesAllowed.value()));
                if( ! isUserAllowed(session, rolesSet)){
                    requestContext.abortWith(ACCESS_DENIED);
                    return;
                }
            }
        }
    
    }

    private boolean isUserAllowed(String session, Set<String> rolesSet) {
//        System.out.println("session="+session+", rolesSet="+rolesSet);
        User user = userService.getBySessionId(session);
//        System.out.println("user="+user);
        if(user!=null){
            UserRole userRole = userRoleService.getByLogin(user.getLogin());
//            System.out.println("userRole="+userRole);
            if(rolesSet.contains(userRole.getRole().getRoleName())){
//                System.out.println("reurn="+true);
                return true;
            }
        }
//        System.out.println("reurn="+false);
        return false;
    }
    
}
