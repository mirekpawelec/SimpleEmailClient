/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.converter;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import pl.pawelec.simpleemailclient.model.User;

/**
 *
 * @author mirek
 */
public class UserToJsonConverter extends JsonSerializer<User>{

    @Override
    public void serialize(User user, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeStartObject();
            jg.writeFieldName("id");
            jg.writeNumber(user.getId());
            jg.writeFieldName("email");
            jg.writeString(user.getEmailAccount().getEmailName());
            jg.writeFieldName("fullName");
            jg.writeString(user.getFullName());
        jg.writeEndObject();

//        jg.writeNumber(user.getId());
//        jg.writeString(user.getEmailAccount().getEmailName());
    }
    
}
