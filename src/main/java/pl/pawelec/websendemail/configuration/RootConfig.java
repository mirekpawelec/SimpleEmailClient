/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.configuration;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import pl.pawelec.websendemail.controller.rest.MailController;
import pl.pawelec.websendemail.exception.EmailNoSentExceptionMapper;
import pl.pawelec.websendemail.filter.CORSFilter;
import pl.pawelec.websendemail.model.dao.impl.MailDaoImpl;
import pl.pawelec.websendemail.utils.MailMessageBodyReaderJson;
import pl.pawelec.websendemail.validation.MailValidator;

/**
 *
 * @author mirek
 */
@ApplicationPath("/")
public class RootConfig extends Application{

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> restClazzSet = new HashSet<Class<?>>();
        restClazzSet.add(MailController.class);
        restClazzSet.add(CORSFilter.class);
        restClazzSet.add(MailValidator.class);
        restClazzSet.add(EmailNoSentExceptionMapper.class);
        restClazzSet.add(MailMessageBodyReaderJson.class);
        return restClazzSet;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletonSet = new HashSet<Object>();
        singletonSet.add(MailDaoImpl.getInstance());
        return singletonSet;
    }

}
