 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import pl.pawelec.simpleemailclient.model.enum_.EmailSeparator;

/**
 *
 * @author mirek
 */
public class VerifyEmailPatternValidation implements ConstraintValidator<VerifyEmailPattern, Object>{
    private Pattern pattern;
    
    @Override
    public void initialize(VerifyEmailPattern constraintAnnotation) {
        pattern = Pattern.compile(constraintAnnotation.regexp());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String text = (String) value;
        boolean result = true;
//        System.out.println("isValid() : text="+text);
        if(text!=null && !text.isEmpty()){
            String[] emailAddresses = text.split(EmailSeparator.REG_EX.getSeparator());
            for(String emailAddress : emailAddresses){
//                System.out.println("isValid() : emailAddress="+emailAddress);
                if(emailAddress!=null && emailAddress.length()>0){
                    Matcher matcher = pattern.matcher(emailAddress.trim());
                    if( ! matcher.matches()){
                        result = false;
                        break;
                    }
                }else{
                    result = false;
                }
            }
        }
//        System.out.println("isValid() : result="+result);
        return result;
    }
    
}
