/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
        String text = value.toString();
        if(!text.isEmpty()){
            Matcher matcher = pattern.matcher(value.toString());
            return matcher.matches();
        }
        return true;
    }
    
}
