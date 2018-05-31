/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.validation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import pl.pawelec.simpleemailclient.model.Attachment;

/**
 *
 * @author mirek
 */
public class VerifySizeValidation implements ConstraintValidator<VerifySize, Set<Attachment>>{
    private static final long _1MB = 1048576;
    private long acceptableSize;
    
    @Override
    public void initialize(VerifySize constraintAnnotation) {
        double maxSize = Double.parseDouble(constraintAnnotation.maxSizeOfMB());
        acceptableSize = (long) (maxSize * _1MB);
    }

    @Override
    public boolean isValid(Set<Attachment> attachmentsSet, ConstraintValidatorContext context) {
        boolean result = false;
        if( ! attachmentsSet.isEmpty()){
            long invalidFiles = attachmentsSet.stream().filter((t) -> {
                return t.getSize()>acceptableSize;
            }).count();
            if(invalidFiles <= 0) result = true;
        }else{
            result = true;
        }
        return result;
    }
    
}
