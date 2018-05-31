/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author mirek
 */
public class VerifyStatusValidation implements ConstraintValidator<VerifyStatus, Object>{
    Class<? extends Enum<?>> enumClass;
    
    @Override
    public void initialize(VerifyStatus constraintAnnotation) {
        enumClass = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(enumClass!=null && value!=null){
            Enum[] enumValues = enumClass.getEnumConstants();
            for(Enum enumItem : enumValues){
               if(enumItem.toString().equals(value.toString())){
                   return true;
               } 
            }
        }
        return false;
    }
    
}
