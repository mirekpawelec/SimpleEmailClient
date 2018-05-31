/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.resteasy.util.Base64;

/**
 *
 * @author mirek
 */
public class Encryption {
    public static String encodeMd5(String dane){
        String result = "";
        MessageDigest md5;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch(NoSuchAlgorithmException ae){
            System.err.println("It's occurred an error while encode the password. \n" + ae.getMessage());
            return result;
        }
        md5.update(dane.getBytes());
        BigInteger hash = new BigInteger(1, md5.digest());
        result = hash.toString(16);
        if(result.length()==31)
            result = "0" + result;
        return result;
    }
    
    public static String encodeBase64(String text){
        return Base64.encodeBytes(text.getBytes(StandardCharsets.UTF_8));
    }
    
    public static String decodeBase64(String text){
        try {
            return new String(Base64.decode(text), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.err.println("decodeBase64() : "+ex.getMessage());
            return "";
        }
    }
}
