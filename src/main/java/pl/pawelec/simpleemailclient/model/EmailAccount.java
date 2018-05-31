/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model;

import java.io.IOException;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.jboss.resteasy.util.Base64;
import pl.pawelec.simpleemailclient.converter.LocalDateTimeToDateTimeConverter;

/**
 *
 * @author mirek
 */
@Entity
@Table(name = "user_email_account")
@XmlRootElement
public class EmailAccount implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "email_name", nullable = false, length = 100)
    private String emailName;
    @Column(name = "email_password", nullable = false, length = 100)
    private String emailPassword;
    @Column(name = "smtp_address", nullable = false, length = 100)
    private String smtpAddress;
    @Column(name = "smtp_port_no", nullable = false)
    private Integer smtpPortNo;
    @Column(name = "pop3_address", nullable = false, length = 100)
    private String pop3Address;
    @Column(name = "pop3_port_no", nullable = false)
    private Integer pop3PortNo;
    @Convert(converter = LocalDateTimeToDateTimeConverter.class)
    @Column(name = "lm_date")
    private LocalDateTime lastModificationDate;
    @Convert(converter = LocalDateTimeToDateTimeConverter.class)
    @Column(name = "c_date")
    private LocalDateTime createDate;

    public EmailAccount() {
        this.lastModificationDate = LocalDateTime.now();
        this.createDate = LocalDateTime.now();
    }

    public EmailAccount(User user, String firstName, String lastName, String emailName, String emailPassword, String smtpAddress, Integer smtpPortNo, String pop3Address, Integer pop3PortNo) {
        this();
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailName = emailName;
        this.emailPassword = Base64.encodeBytes(emailPassword.getBytes());
        this.smtpAddress = smtpAddress;
        this.smtpPortNo = smtpPortNo;
        this.pop3Address = pop3Address;
        this.pop3PortNo = pop3PortNo;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailName() {
        return emailName;
    }

    public void setEmailName(String emailName) {
        this.emailName = emailName;
    }

    public String getEmailPassword() throws IOException {
        return new String(Base64.decode(emailPassword));
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = Base64.encodeBytes(emailPassword.getBytes());
    }

    public String getSmtpAddress() {
        return smtpAddress;
    }

    public void setSmtpAddress(String smtpAddress) {
        this.smtpAddress = smtpAddress;
    }

    public Integer getSmtpPortNo() {
        return smtpPortNo;
    }

    public void setSmtpPortNo(Integer smtpPortNo) {
        this.smtpPortNo = smtpPortNo;
    }

    public String getPop3Address() {
        return pop3Address;
    }

    public void setPop3Address(String pop3Address) {
        this.pop3Address = pop3Address;
    }

    public Integer getPop3PortNo() {
        return pop3PortNo;
    }

    public void setPop3PortNo(Integer pop3PortNo) {
        this.pop3PortNo = pop3PortNo;
    }

    public LocalDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(LocalDateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.user);
        hash = 37 * hash + Objects.hashCode(this.firstName);
        hash = 37 * hash + Objects.hashCode(this.lastName);
        hash = 37 * hash + Objects.hashCode(this.emailName);
        hash = 37 * hash + Objects.hashCode(this.emailPassword);
        hash = 37 * hash + Objects.hashCode(this.smtpAddress);
        hash = 37 * hash + Objects.hashCode(this.smtpPortNo);
        hash = 37 * hash + Objects.hashCode(this.pop3Address);
        hash = 37 * hash + Objects.hashCode(this.pop3PortNo);
        hash = 37 * hash + Objects.hashCode(this.lastModificationDate);
        hash = 37 * hash + Objects.hashCode(this.createDate);
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
        final EmailAccount other = (EmailAccount) obj;
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.emailName, other.emailName)) {
            return false;
        }
        if (!Objects.equals(this.emailPassword, other.emailPassword)) {
            return false;
        }
        if (!Objects.equals(this.smtpAddress, other.smtpAddress)) {
            return false;
        }
        if (!Objects.equals(this.pop3Address, other.pop3Address)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.smtpPortNo, other.smtpPortNo)) {
            return false;
        }
        if (!Objects.equals(this.pop3PortNo, other.pop3PortNo)) {
            return false;
        }
        if (!Objects.equals(this.lastModificationDate, other.lastModificationDate)) {
            return false;
        }
        if (!Objects.equals(this.createDate, other.createDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserEmailAccount{" 
                + "id=" + id 
                + ", userId=" + user.getId()
                + ", firstName=" + firstName 
                + ", lastName=" + lastName 
                + ", emailName=" + emailName 
                + ", emailPassword=" + emailPassword 
                + ", smtpAddress=" + smtpAddress 
                + ", smtpPortNo=" + smtpPortNo 
                + ", pop3Address=" + pop3Address 
                + ", pop3PortNo=" + pop3PortNo 
                + ", lastModificationDate=" + lastModificationDate 
                + ", createDate=" + createDate 
                + '}';
    }
    
}
