/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.configuration;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import pl.pawelec.simpleemailclient.controller.rest.LoginController;
import pl.pawelec.simpleemailclient.controller.rest.EmailController;
import pl.pawelec.simpleemailclient.controller.rest.FilesServiceController;
import pl.pawelec.simpleemailclient.exception.EmailExceptionMapper;
import pl.pawelec.simpleemailclient.interceptor.CORSFilter;
import pl.pawelec.simpleemailclient.interceptor.CharsetRequestFilter;
import pl.pawelec.simpleemailclient.interceptor.SecurityInterceptor;
import pl.pawelec.simpleemailclient.utils.EmaiJsonlMessageBodyReader;
import pl.pawelec.simpleemailclient.utils.EmailMultipartFormDataMessageBodyReader;
import pl.pawelec.simpleemailclient.validation.ResteasyViolationException;

/**
 *
 * @author mirek
 */
@ApplicationPath("/rest")
public class RootConfig extends Application{

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> restClazzSet = new HashSet<Class<?>>();
        restClazzSet.add(EmailController.class);
        restClazzSet.add(LoginController.class);
        restClazzSet.add(FilesServiceController.class);
        restClazzSet.add(ResteasyViolationException.class);
//        restClazzSet.add(EmailExceptionMapper.class);
        restClazzSet.add(CORSFilter.class);
        restClazzSet.add(SecurityInterceptor.class);
        restClazzSet.add(CharsetRequestFilter.class);
        restClazzSet.add(EmailMultipartFormDataMessageBodyReader.class);
        restClazzSet.add(EmaiJsonlMessageBodyReader.class);
        return restClazzSet;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletonSet = new HashSet<Object>();
//        singletonSet.add(EmailDaoImpl.getInstance());
        return singletonSet;
    }

}
