/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model;


import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import pl.pawelec.simpleemailclient.converter.UserToJsonConverter;
import pl.pawelec.simpleemailclient.model.enum_.Age;
import pl.pawelec.simpleemailclient.model.enum_.Folder;
import pl.pawelec.simpleemailclient.model.enum_.MessageType;
import pl.pawelec.simpleemailclient.model.enum_.Status;
import pl.pawelec.simpleemailclient.utils.Encryption;
import pl.pawelec.simpleemailclient.validation.VerifyEmailPattern;
import pl.pawelec.simpleemailclient.validation.VerifyFolder;
import pl.pawelec.simpleemailclient.validation.VerifySize;
import pl.pawelec.simpleemailclient.validation.VerifyStatus;

/**
 *
 * @author mirek
 */
@Entity
@Table(name = "emails")
@XmlRootElement
public class Email implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @VerifyEmailPattern(
            regexp = "^(([^<>\\[\\]@]+<(\\w+[+.-]{1}){0,}\\w+@(\\w+[.-]{1}){0,}\\w+\\.\\w{2,}>)|((\\w+[+.-]{1}){0,}\\w+@(\\w+[.-]{1}){0,}\\w+\\.\\w{2,}))$", 
            message = "Nieodpowiedni format. Popraw wg wzoru \"Jan Nowak &lt;jan@nowak.com&gt;\", lub \"jan@nowak.com\". Usuń polskie znaki, adresy rozdziel \",\" przecinkiem.")
    @Transient
    private String sender;
    @VerifyEmailPattern(
            regexp = "^(([^<>\\[\\]@]+<(\\w+[+.-]{1}){0,}\\w+@(\\w+[.-]{1}){0,}\\w+\\.\\w{2,}>)|((\\w+[+.-]{1}){0,}\\w+@(\\w+[.-]{1}){0,}\\w+\\.\\w{2,}))$",
            message = "Nieodpowiedni format. Popraw wg wzoru \"Jan Nowak &lt;jan@nowak.com&gt;\", lub \"jan@nowak.com\". Usuń polskie znaki, adresy rozdziel \",\" przecinkiem.")
    @Transient
    private String recipient;
    @VerifyEmailPattern(
            regexp = "^(([^<>\\[\\]@]+<(\\w+[+.-]{1}){0,}\\w+@(\\w+[.-]{1}){0,}\\w+\\.\\w{2,}>)|((\\w+[+.-]{1}){0,}\\w+@(\\w+[.-]{1}){0,}\\w+\\.\\w{2,}))$",
            message = "Nieodpowiedni format. Popraw wg wzoru \"Jan Nowak &lt;jan@nowak.com&gt;\", lub \"jan@nowak.com\". Usuń polskie znaki, adresy rozdziel \",\" przecinkiem.")
    @Transient
    private String additionalRecipient;
    @Column(name="title", length = 500)
    private String title;
    @NotNull(message = "Zawartosć nie może być pusta!")
    @Lob
    @Column(name="message")
    private String message;
    @VerifyFolder(value = Folder.class, message = "Wskazany folder nie istnieje!")
    @Column(name="folder", nullable = false, length = 25)
    private String folder;
    @VerifyStatus(value = Status.class, message = "Podany status jest niepoprawny! Użyj YES, lub NO.")
    @Column(name="flag", nullable = false, length = 10)
    private String flag;
    @VerifyStatus(value = Status.class, message = "Podany status jest niepoprawny! Użyj YES, lub NO.")
    @Column(name="reading_confirmation", nullable = false, length = 10)
    private String readingConfirmation;
    @VerifyStatus(value = Age.class, message = "Podany status jest niepoprawny! Użyj NEW, lub OLD.")
    @Column(name="status", nullable = false, length = 10)
    private String status;
    @VerifyStatus(value = MessageType.class, message = "Podany typ jest niepoprawny! Użyj INCOMING, lub ONGOING.")
    @Column(name="message_type", nullable = false, length = 10)
    private String messageType;
    @Column(name = "c_date")
    private Long createDate;
    @Column(name="attachment_name", length = 500)
    private String attachmentName;
    @Transient
    @VerifySize(maxSizeOfMB = "10", message = "Jeden, lub więcej plików przekroczył dopuszczalny rozmiar (max 10MB).")
    private Set<Attachment> attachments;
    @OneToMany(mappedBy = "email", fetch = FetchType.EAGER)
    private Set<EmailInternetAddress> emailInternetAddressesSet;
    
    public Email() {
        this.folder = Folder.DRAFT.name();
        this.flag = Status.NO.name();
        this.readingConfirmation = Status.NO.name();
        this.status = Age.NEW.name();
        this.messageType = MessageType.INCOMING.name();
        this.createDate = System.currentTimeMillis();
        this.attachments = new HashSet<Attachment>();
        this.emailInternetAddressesSet = new HashSet<EmailInternetAddress>();
    }

    public Email(User user,
                 String sender,
                 String recipientEmail, 
                 String additionalRecipientEmail, 
                 String title, 
                 String message,
                 String folder) {
        this();
        this.user = user;
        this.sender = sender;
        this.recipient = recipientEmail;
        this.additionalRecipient = additionalRecipientEmail;
        this.title = title;
        this.message = message;
        this.folder = folder;
    }

    public Email(Builder builder) {
        this.id = builder.id;
        this.user = builder.user;
        this.sender = builder.sender;
        this.recipient = builder.recipient;
        this.additionalRecipient = builder.additionalRecipient;
        this.title = builder.title;
        this.message = builder.message;
        this.folder = builder.folder;
        this.flag = builder.flag;
        this.readingConfirmation = builder.readingConfirmation;
        this.status = builder.status;
        this.messageType = builder.messageType;
        this.createDate = builder.createDate;
        this.attachmentName = builder.attachmentName;
        this.attachments = builder.attachments;
        this.emailInternetAddressesSet = builder.emailInternetAddressSet;
    }

    @JsonIgnore
    public boolean isNew(){
        return this.id==null;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @JsonSerialize(using = UserToJsonConverter.class)
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getAdditionalRecipient() {
        return additionalRecipient;
    }
    public void setAdditionalRecipient(String ccEmail) {
        this.additionalRecipient = ccEmail;
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

    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getReadingConfirmation() {
        return readingConfirmation;
    }
    public void setReadingConfirmation(String readingConfirmation) {
        this.readingConfirmation = readingConfirmation;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @JsonIgnore
    public String getMessageType() {
        return messageType;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    
    public Long getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }
    
    public String getAttachmentName() {
        return attachmentName;
    }
    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }
    
    @JsonIgnore
    public Set<Attachment> getAttachments() {
        return attachments;
    }
    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }
    
    @JsonIgnore
    public Set<EmailInternetAddress> getEmailInternetAddressesSet() {
        return emailInternetAddressesSet;
    }
    public void setEmailInternetAddressesSet(Set<EmailInternetAddress> emailInternetAddressesSet) {
        this.emailInternetAddressesSet = emailInternetAddressesSet;
    }
    
    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }
    
    /**
     * Method create string characters of all names of files of attachment.
     * @param startText , value will be added to the start of a string
     */
    public void buildAttachmentName(){
        final String SEPARATOR = "; ";
        StringBuffer stringBuffer = new StringBuffer();
        this.attachments.forEach(a->{
            stringBuffer.append(a.getName()+SEPARATOR);
        });
        if(stringBuffer.length()>0){
            this.attachmentName = stringBuffer.toString().substring(0, stringBuffer.length()-SEPARATOR.length());
        }else{
            this.attachmentName = "";
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.user);
        hash = 29 * hash + Objects.hashCode(this.sender);
        hash = 29 * hash + Objects.hashCode(this.recipient);
        hash = 29 * hash + Objects.hashCode(this.additionalRecipient);
        hash = 29 * hash + Objects.hashCode(this.title);
        hash = 29 * hash + Objects.hashCode(this.message);
        hash = 29 * hash + Objects.hashCode(this.folder);
        hash = 29 * hash + Objects.hashCode(this.flag);
        hash = 29 * hash + Objects.hashCode(this.readingConfirmation);
        hash = 29 * hash + Objects.hashCode(this.status);
        hash = 29 * hash + Objects.hashCode(this.messageType);
        hash = 29 * hash + Objects.hashCode(this.createDate);
        hash = 29 * hash + Objects.hashCode(this.attachmentName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Email other = (Email) obj;
        if (!Objects.equals(this.id, other.id)) return false;
        if (!Objects.equals(this.user, other.user)) return false;
        if (!Objects.equals(this.sender, other.sender)) return false;
        if (!Objects.equals(this.recipient, other.recipient)) return false;
        if (!Objects.equals(this.additionalRecipient, other.additionalRecipient)) return false;
        if (!Objects.equals(this.title, other.title)) return false;
        if (!Objects.equals(this.message, other.message)) return false;
        if (!Objects.equals(this.folder, other.folder)) return false;
        if (!Objects.equals(this.flag, other.flag)) return false;
        if (!Objects.equals(this.readingConfirmation, other.readingConfirmation)) return false;
        if (!Objects.equals(this.status, other.status)) return false;
        if (!Objects.equals(this.messageType, other.messageType)) return false;
        if (!Objects.equals(this.attachmentName, other.attachmentName)) return false;
        if (!Objects.equals(this.createDate, other.createDate)) return false;
        return true;
    }


    
    public static class Builder{
        private Long id;
        private User user;
        private String sender;
        private String recipient;
        private String additionalRecipient;
        private String title;
        private String message;
        private String folder;
        private String flag;
        private String readingConfirmation;
        private String status;
        private String messageType;
        private Long createDate;
        private String attachmentName;
        private Set<Attachment> attachments = new HashSet<>();
        private Set<EmailInternetAddress> emailInternetAddressSet = new HashSet<>();
        public Builder withId(Long id){
            this.id=id;
            return this;
        }
        public Builder withUser(User user){
            this.user=user;
            return this;
        }
        public Builder withSender(String sender){
            this.sender=sender;
            return this;
        }
        public Builder withRecipient(String recipient){
            this.recipient=recipient;
            return this;
        }
        public Builder withAdditionalRecipient(String additionalRecipient){
            this.additionalRecipient=additionalRecipient;
            return this;
        }
        public Builder withTitle(String title){
            this.title=title;
            return this;
        }
        public Builder withMessage(String message){
            this.message=message;
            return this;
        }
        public Builder withFolder(String folder){
            this.folder=folder;
            return this;
        }
        public Builder withFlag(String flag){
            this.flag=flag;
            return this;
        }
        public Builder withReadingConfirmation(String readingConfirmation){
            this.readingConfirmation=readingConfirmation;
            return this;
        }
        public Builder withStatus(String status){
            this.status=status;
            return this;
        }
        public Builder withMessageType(String messageType){
            this.messageType=messageType;
            return this;
        }
        public Builder withCreateDate(Long createDate){
            this.createDate=createDate;
            return this;
        }
        public Builder withAttachmentName(String attachmentName){
            this.attachmentName=attachmentName;
            return this;
        }
        public Builder withAttachments(Set<Attachment> attachments){
            this.attachments=attachments;
            return this;
        }
        public Builder withEmailInternetAddressSet(Set<EmailInternetAddress> emailInternetAddressSet){
            this.emailInternetAddressSet=emailInternetAddressSet;
            return this;
        }
        public Email build(){
            return new Email(this);
        }
    }

    @Override
    public String toString() {
        return "Email{" + "id=" + id 
                + ", userId=" + (user!=null?user.getId():"null") 
                + ", sender=" + sender 
                + ", recipient=" + recipient 
                + ", additionalRecipient=" + additionalRecipient 
                + ", title=" + title 
                + ", message=" + message 
                + ", folder=" + folder 
                + ", flag=" + flag 
                + ", readingConfirmation=" + readingConfirmation 
                + ", status=" + status 
                + ", messageType=" + messageType 
                + ", createDate=" + Instant.ofEpochMilli(createDate).atZone(ZoneId.systemDefault()).toLocalDateTime() 
                + ", attachmentName=" + attachmentName 
                + ", sttachmentsCount=" + attachments.size() 
                + ", emailInternetAddressesCount=" + emailInternetAddressesSet.size()
                + '}';
    }
}
