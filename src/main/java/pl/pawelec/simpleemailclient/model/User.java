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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
import pl.pawelec.simpleemailclient.converter.LocalDateTimeToDateTimeConverter;
import pl.pawelec.simpleemailclient.utils.Encryption;

/**
 *
 * @author mirek
 */

@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByLogin", query = "SELECT u FROM User u WHERE u.login = :login"),
    @NamedQuery(name = "User.findBySessionId", query = "SELECT u FROM User u WHERE u.sessionId = :sessionId")
})
@XmlRootElement
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false, unique = true)
    private String login;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(name = "full_name", length = 150)
    private String fullName;
    @Column(name = "session_id", length = 150)
    private String sessionId;
    @Column(name = "expiration_date")
    private Long expirationDate;
    @Convert(converter = LocalDateTimeToDateTimeConverter.class)
    @Column(name = "ll_date")
    private LocalDateTime lastLoginDate;
    @Convert(converter = LocalDateTimeToDateTimeConverter.class)
    @Column(name = "c_date")
    private LocalDateTime createDate;
    
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private EmailAccount emailAccount;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Email> emailsSet;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Contact> contactsSet;
    
    public User() {
        this.createDate = LocalDateTime.now();
        this.emailAccount = new EmailAccount();
        this.emailsSet = new HashSet<Email>();
        this.contactsSet = new HashSet<Contact>();
    }

    public User(String login, String password, String fullName) {
        this();
        this.login = login;
        this.password = Encryption.encodeMd5(password);
        this.fullName = fullName;
    }

    public User(String login, String password, String fullName, String sessionId, Long expirationDate) {
        this();
        this.login = login;
        this.password = Encryption.encodeMd5(password);
        this.fullName = fullName;
        this.sessionId = sessionId;
        this.expirationDate = expirationDate;
        this.lastLoginDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = Encryption.encodeMd5(password);
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }
    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @JsonIgnore
    public EmailAccount getEmailAccount() {
        return emailAccount;
    }
    public void setEmailAccount(EmailAccount emailAccount) {
        this.emailAccount = emailAccount;
    }

    @JsonIgnore
    public Set<Email> getEmailsSet() {
        return emailsSet;
    }
    public void setEmailsSet(Set<Email> emailsSet) {
        this.emailsSet = emailsSet;
    }

    @JsonIgnore
    public Set<Contact> getContactsSet() {
        return contactsSet;
    }
    public void setContactsSet(Set<Contact> contactsSet) {
        this.contactsSet = contactsSet;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.login);
        hash = 97 * hash + Objects.hashCode(this.password);
        hash = 97 * hash + Objects.hashCode(this.fullName);
        hash = 97 * hash + Objects.hashCode(this.sessionId);
        hash = 97 * hash + Objects.hashCode(this.expirationDate);
        hash = 97 * hash + Objects.hashCode(this.lastLoginDate);
        hash = 97 * hash + Objects.hashCode(this.createDate);
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
        final User other = (User) obj;
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.fullName, other.fullName)) {
            return false;
        }
        if (!Objects.equals(this.sessionId, other.sessionId)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.expirationDate, other.expirationDate)) {
            return false;
        }
        if (!Objects.equals(this.lastLoginDate, other.lastLoginDate)) {
            return false;
        }
        if (!Objects.equals(this.createDate, other.createDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" 
               + "id=" + id 
               + ", login=" + login 
               + ", password=" + password 
               + ", fullName=" + fullName 
               + ", sessionId=" + sessionId 
               + ", expirationDate=" + expirationDate
               + ", lastLoginDate=" + lastLoginDate 
               + ", createDate=" + createDate
               + ", emailAccountId=" + (emailAccount!=null?emailAccount.getId():"")
               + ", emailsSetSize=" + emailsSet.size()
               + ", contactsSetSize=" + contactsSet.size()
               + '}';
    }
    
}
