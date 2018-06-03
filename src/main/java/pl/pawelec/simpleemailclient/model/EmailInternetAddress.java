/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import pl.pawelec.simpleemailclient.converter.LocalDateTimeToDateTimeConverter;
import pl.pawelec.simpleemailclient.model.enum_.RecipientType;

/**
 *
 * @author mirek
 */
@Entity
@Table(name="email_internet_addresses", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"email_id", "contact_id", "recipient_type"})
       })
@XmlRootElement
public class EmailInternetAddress implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "email_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Email email;
    @JoinColumn(name = "contact_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Contact contact;
    @Column(name = "recipient_type", nullable = false, length = 10)
    private String type;
    @Convert(converter = LocalDateTimeToDateTimeConverter.class)
    @Column(name = "c_date")
    private LocalDateTime createDate;

    public EmailInternetAddress() {
        this.email = new Email();
        this.contact = new Contact();
        this.createDate = LocalDateTime.now();
    }

    public EmailInternetAddress(Email email, Contact contact, String type) {
        this();
        this.email = email;
        this.contact = contact;
        this.type = type;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Email getEmail() {
        return email;
    }
    public void setEmail(Email email) {
        this.email = email;
    }

    public Contact getContact() {
        return contact;
    }
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.email);
        hash = 67 * hash + Objects.hashCode(this.contact);
        hash = 67 * hash + Objects.hashCode(this.type);
        hash = 67 * hash + Objects.hashCode(this.createDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final EmailInternetAddress other = (EmailInternetAddress) obj;
        if (!Objects.equals(this.type, other.type)) return false;
        if (!Objects.equals(this.id, other.id)) return false;
        if (!Objects.equals(this.email, other.email)) return false;
        if (!Objects.equals(this.contact, other.contact)) return false;
        if (!Objects.equals(this.createDate, other.createDate)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "InternetAddress{" 
                + "id=" + id 
                + ", email=" + email.getId()
                + ", contact=" + contact.getId()
                + ", type=" + type 
                + ", createDate=" + createDate 
                + '}';
    }
      
}
