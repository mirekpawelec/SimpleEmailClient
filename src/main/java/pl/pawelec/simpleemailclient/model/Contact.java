/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import pl.pawelec.simpleemailclient.converter.LocalDateTimeToDateTimeConverter;
import pl.pawelec.simpleemailclient.model.enum_.ContactStatus;

/**
 *
 * @author mirek
 */
@Entity
@Table(name="contacts", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"user_id", "name", "email_address"})
       })
@XmlRootElement
public class Contact implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @Column(name = "name", nullable = true, length = 100)
    private String name;
    @Column(name = "email_address", nullable = false, length = 100)
    private String emailAddress;
    @Column(name = "status", nullable = false, length = 10)
    private String status;
    @Convert(converter = LocalDateTimeToDateTimeConverter.class)
    @Column(name = "c_date")
    private LocalDateTime createDate;
    @OneToMany(mappedBy = "contact", fetch = FetchType.EAGER)
    private Set<EmailInternetAddress> emailInternetAddressesSet;
            
    public Contact() {
        this.user = new User();
        this.status = ContactStatus.OK.name();
        this.createDate = LocalDateTime.now();
        this.emailInternetAddressesSet = new HashSet<EmailInternetAddress>();
    }

    public Contact(User user, String name, String emailAddress) {
        this();
        this.user = user;
        this.name = name;
        this.emailAddress = emailAddress;        
    }

    public boolean isNew(){
        return id==null;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Set<EmailInternetAddress> getEmailInternetAddressesSet() {
        return emailInternetAddressesSet;
    }
    public void setEmailInternetAddressesSet(Set<EmailInternetAddress> emailInternetAddressesSet) {
        this.emailInternetAddressesSet = emailInternetAddressesSet;
    }

    public String concatenateNameWithEmailAddress(){
        StringBuffer buffer = new StringBuffer();
        if( ! this.name.isEmpty()){
            buffer.append(this.name);
            buffer.append(" <"+this.emailAddress+">");
        }else{
            buffer.append(this.emailAddress);
        }
        return buffer.toString();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.user);
        hash = 97 * hash + Objects.hashCode(this.emailAddress);
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.status);
        hash = 97 * hash + Objects.hashCode(this.createDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Contact other = (Contact) obj;
        if (!Objects.equals(this.id, other.id)) return false;
        if (!Objects.equals(this.user, other.user)) return false;
        if (!Objects.equals(this.name, other.name)) return false;
        if (!Objects.equals(this.emailAddress, other.emailAddress)) return false;
        if (!Objects.equals(this.status, other.status)) return false;
        if (!Objects.equals(this.createDate, other.createDate)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Contact{" 
                + "id=" + id 
                + ", user=" + user.getId()
                + ", name=" + name 
                + ", emailAddress=" + emailAddress 
                + ", status=" + status
                + ", createDate=" + createDate 
                + ", emailInternetAddressesSet=" + emailInternetAddressesSet.size()
                + '}';
    }
    
}
