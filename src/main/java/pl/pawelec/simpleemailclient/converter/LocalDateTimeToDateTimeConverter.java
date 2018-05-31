/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author mirek
 */
@Converter(autoApply = true)
public class LocalDateTimeToDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp>{
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        return attribute==null? null: Timestamp.valueOf(attribute);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        return dbData==null ? null : dbData.toLocalDateTime();
    }
}
