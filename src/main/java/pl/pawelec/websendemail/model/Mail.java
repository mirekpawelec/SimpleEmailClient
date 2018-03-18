/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.websendemail.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.Length;
import pl.pawelec.websendemail.model.enum_.Folder;
import pl.pawelec.websendemail.model.enum_.Status;
import pl.pawelec.websendemail.validation.VerifyEmailPattern;
import pl.pawelec.websendemail.validation.VerifyFolder;
import pl.pawelec.websendemail.validation.VerifyStatus;

/**
 *
 * @author mirek
 */
@XmlRootElement
public class Mail {

    private Long id;
    @NotNull(message = "Dodaj adres odbiorcy.")
    @Pattern(regexp = "^.*@.+[.].+$", message = "Niepoprawny format adresu email.")
    private String recipientEmail;
    @VerifyEmailPattern(regexp = "^.+@.+[.].+$", message = "Niepoprawny format adresu email.")
    private String additionalRecipientEmail;
    private String title;
    @NotNull(message = "Zawartosć nie może być pusta!")
    @Length(min = 1, max = 1000, message = "Niepoprawna długość wiadomości - min 1, max 1000 znaków.")
    private String message;
    @VerifyFolder(value = Folder.class, message = "Wskazany folder nie istnieje!")
    private String folder;
    @VerifyStatus(value = Status.class, message = "Podany status jest niepoprawny! Użyj YES, lub NO.")
    private String importantMessage;
    @VerifyStatus(value = Status.class, message = "Podany status jest niepoprawny! Użyj YES, lub NO.")
    private String readingConfirmation;
    private Long createDate;
    
    public Mail() {
        this.importantMessage = Status.NO.toString();
        this.readingConfirmation = Status.NO.toString();
        this.createDate = LocalDateTime.now().toInstant(ZoneOffset.ofHours(1)).toEpochMilli(); 
    }

    public Mail(String recipientEmail, 
                String additionalRecipientEmail, 
                String title, 
                String message,
                String folder) {
        this();
        this.recipientEmail = recipientEmail;
        this.additionalRecipientEmail = additionalRecipientEmail;
        this.title = title;
        this.message = message;
        this.folder = folder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getAdditionalRecipientEmail() {
        return additionalRecipientEmail;
    }

    public void setAdditionalRecipientEmail(String ccEmail) {
        this.additionalRecipientEmail = ccEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getImportantMessage() {
        return importantMessage;
    }

    public void setImportantMessage(String importantMessage) {
        this.importantMessage = importantMessage;
    }

    public String getReadingConfirmation() {
        return readingConfirmation;
    }

    public void setReadingConfirmation(String readingConfirmation) {
        this.readingConfirmation = readingConfirmation;
    }

    public Long getCreateDate() {
        return createDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.recipientEmail);
        hash = 79 * hash + Objects.hashCode(this.additionalRecipientEmail);
        hash = 79 * hash + Objects.hashCode(this.title);
        hash = 79 * hash + Objects.hashCode(this.message);
        hash = 79 * hash + Objects.hashCode(this.folder);
        hash = 79 * hash + Objects.hashCode(this.importantMessage);
        hash = 79 * hash + Objects.hashCode(this.readingConfirmation);
        hash = 79 * hash + Objects.hashCode(this.createDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mail other = (Mail) obj;
        if (!Objects.equals(this.recipientEmail, other.recipientEmail)) {
            return false;
        }
        if (!Objects.equals(this.additionalRecipientEmail, other.additionalRecipientEmail)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        if (!Objects.equals(this.folder, other.folder)) {
            return false;
        }
        if (!Objects.equals(this.importantMessage, other.importantMessage)) {
            return false;
        }
        if (!Objects.equals(this.readingConfirmation, other.readingConfirmation)) {
            return false;
        }
        if (!Objects.equals(this.createDate, other.createDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mail{" + "  id=" + id 
                       + ", recipientEmail=" + recipientEmail 
                       + ", additionalRecipientEmail=" + additionalRecipientEmail 
                       + ", title=" + title 
                       + ", message=" + message 
                       + ", folder=" + folder
                       + ", createDate=" + Instant.ofEpochMilli(createDate).atZone(ZoneId.systemDefault()).toLocalDateTime()
                       + ", importantMessage=" + importantMessage
                       + ", readingConfirmation=" + readingConfirmation + '}';
    }
    
}
