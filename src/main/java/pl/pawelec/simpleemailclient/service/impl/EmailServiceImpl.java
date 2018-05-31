/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import pl.pawelec.simpleemailclient.exception.EmailNoFoundException;
import pl.pawelec.simpleemailclient.exception.FlagNoChangeException;
import pl.pawelec.simpleemailclient.exception.ReadStatusNoChangeException;
import pl.pawelec.simpleemailclient.model.Attachment;
import pl.pawelec.simpleemailclient.model.Contact;
import pl.pawelec.simpleemailclient.model.Email;
import pl.pawelec.simpleemailclient.model.EmailInternetAddress;
import pl.pawelec.simpleemailclient.model.User;
import pl.pawelec.simpleemailclient.model.dao.EmailDao;
import pl.pawelec.simpleemailclient.model.enum_.Age;
import pl.pawelec.simpleemailclient.model.enum_.EmailSeparator;
import pl.pawelec.simpleemailclient.model.enum_.MessageType;
import pl.pawelec.simpleemailclient.model.enum_.PathToFile;
import pl.pawelec.simpleemailclient.model.enum_.RecipientType;
import pl.pawelec.simpleemailclient.model.enum_.Status;
import pl.pawelec.simpleemailclient.qualifier.ContactServiceQualifier;
import pl.pawelec.simpleemailclient.utils.MailUtils;
import pl.pawelec.simpleemailclient.qualifier.EmailDaoQualifier;
import pl.pawelec.simpleemailclient.qualifier.EmailInternetAddressServiceQualifier;
import pl.pawelec.simpleemailclient.qualifier.EmailServiceQualifier;
import pl.pawelec.simpleemailclient.service.ContactService;
import pl.pawelec.simpleemailclient.service.EmailInternetAddressService;
import pl.pawelec.simpleemailclient.service.EmailService;
import pl.pawelec.simpleemailclient.utils.Encryption;

/**
 *
 * @author mirek
 */
@EmailServiceQualifier
public class EmailServiceImpl implements Serializable, EmailService{
    private static final String EMAIL_ADDRESS = "java.email@wp.pl";
    private static final String EMAIL_PASSWORD = "Java8888";
    private static final String ERROR_INFO_FLAG = "The change of the flag failed!";
    private static final String ERROR_INFO_READ_STATUS = "The change of the read status failed!";
    
    @Inject @EmailDaoQualifier
    private EmailDao emailDao;
    @Inject @ContactServiceQualifier
    private ContactService contactServiceImpl;
    @Inject @EmailInternetAddressServiceQualifier
    private EmailInternetAddressService emailInternetAddressService;
    
    public Email create(Email entity) {
        String storeMessageText = entity.getMessage();
        entity.setMessage( Encryption.encodeBase64(entity.getMessage()) );
        emailDao.create(entity);  
        if( ! entity.isNew()){  // the email entity must have id!
            saveContactsAndEmailInternetAddresses(entity, entity.getUser(), entity.getSender(), RecipientType.FROM);
            saveContactsAndEmailInternetAddresses(entity, entity.getUser(), entity.getRecipient(), RecipientType.TO);
            saveContactsAndEmailInternetAddresses(entity, entity.getUser(), entity.getAdditionalRecipient(), RecipientType.CC);
            saveAttachment(entity); 
        }
        entity.setMessage(storeMessageText);
        return entity;
    }

    public boolean update(Email entity) {
        return emailDao.update(entity);
    }

    public boolean delete(Email entity) {
        boolean continueProcess = true;
        try {
            Path pathToMessageDirectory = Paths.get(PathToFile.MAIN_REPOSITORY.getPath(), entity.getUser().getId()+"", entity.getId()+"");
            for(String f : entity.getAttachmentName().split("; ")){
                Path pathToAttachmentFile = Paths.get(pathToMessageDirectory.toString(), f);
                Files.deleteIfExists(pathToAttachmentFile);
            };
            Files.deleteIfExists(pathToMessageDirectory);
        } catch (IOException ex) {
            System.err.println("Directories haven't failed to delete.");
            continueProcess = false;
        }
        if(continueProcess)
            continueProcess = emailDao.delete(entity);
        return continueProcess;
    }

    public List<Object[]> deletePermanently(List<Object[]> emailsList) {
        emailsList.forEach(e->{
            e[1] = this.delete((Email) e[0]);
        });
        MailUtils.deleteEmail(emailsList);
        return emailsList;
    }
    
    public Email get(Serializable id) {
        return emailDao.get(id);
    }

    public List<Email> getAll() {
        return emailDao.getAll();
    }
    
    public boolean send(Email email) {
        return MailUtils.send(email); 
    }
        
    public String changeFlag(Long id) {
        Email modifyEmail = emailDao.get(id);
        if(modifyEmail.getFlag().equals(Status.NO.name())){
            modifyEmail.setFlag(Status.YES.name());
        }else{
            modifyEmail.setFlag(Status.NO.name());
        }
        if(emailDao.update(modifyEmail)){
            return modifyEmail.getFlag();
        }else{
            throw new FlagNoChangeException(ERROR_INFO_FLAG);
        }
    }

    public void changeReadStatus(Long id, String status) {
        for(Enum enumerates: Status.values()){
            if(enumerates.toString().equals(status)){
                Email modifyEmail = emailDao.get(id);
                if( ! modifyEmail.getReadingConfirmation().equals(status)){
                    modifyEmail.setReadingConfirmation(enumerates.name());
                    if( ! emailDao.update(modifyEmail)) 
                        throw new ReadStatusNoChangeException(ERROR_INFO_READ_STATUS);
                }
                return;
            }
        }
        throw new ReadStatusNoChangeException(ERROR_INFO_READ_STATUS);
    }

    public List<Email> getByFolder(String folderName, Long userId, Integer page, Integer limit) {
        return emailDao.getByFolder(folderName, userId, page, limit);
    }

    public Boolean changeStatus(Long id) {
        try{
            Email email = emailDao.get(id);
            if(email.getStatus().equals(Age.NEW.name())){
                email.setStatus(Age.OLD.name());
            }else{
                email.setStatus(Age.NEW.name());
            }
            return emailDao.update(email);
        }catch(EmailNoFoundException enfe){
            return false;
        }
    }

    public void checkEmailBox(User user) {
        Long maxDate;
        try{
            maxDate = emailDao.getAllByUser(user.getId()).stream()
                .filter(email->email.getMessageType().equals(MessageType.INCOMING.name()))
                .map(Email::getCreateDate)
                .max(Long::compareTo)
                .orElse(0L); 
        }catch(EmailNoFoundException enf){
            maxDate = 0L;
        }
        System.out.println("youngestDateMessage: " + Instant.ofEpochMilli(maxDate).atZone(ZoneId.systemDefault()).toLocalDateTime());
        MailUtils.fetch(user, maxDate).forEach(email->{
//            System.out.println("checkEmailBox() : "+email);
            this.create(email);
        });
    }

    public List<Email> getAllByUser(Long userId) {
        return emailDao.getAllByUser(userId);
    }
    
    public Long count(Long userId, String folderName) {
        return emailDao.count(userId, folderName);
    }
    
    private void saveContactsAndEmailInternetAddresses(Email email, User user, String contactsString, RecipientType addressType){
        Iterator<Contact> iterContact = splitStringToSingleContacts(user, contactsString).iterator();
        while(iterContact.hasNext()){
            Contact contact = iterContact.next();
            try{
                Contact existsContact = contactServiceImpl.get(user.getId(), contact.getName(), contact.getEmailAddress());
                contact.setId(existsContact.getId());
                // if the previous entity not equals the now one, update
//                if( !existsContact.getName().equals(contact.getName()) || !existsContact.getEmailAddress().equals(contact.getEmailAddress())){ 
//                    contactServiceImpl.update(contact);
//                }
            }catch(NoResultException nre){
                contact = contactServiceImpl.create(contact);
            }
            emailInternetAddressService.create(new EmailInternetAddress(email, contact, addressType.name()));
        }
    }
    
    private Set<Contact> splitStringToSingleContacts(User user, String contactsString){
        Set<Contact> contactsSet = new HashSet<>();
        String[] contacts = contactsString.split(EmailSeparator.REG_EX.getSeparator());
        Arrays.stream(contacts).forEach(c->{
            String contact = c.trim();
            if(contact.length()>0){
                String[] partsContact = contact.split("<");
                switch (partsContact.length) {
                    case 1:
                        contactsSet.add(new Contact(user, "", partsContact[0]));
                        break;
                    case 2:
                        contactsSet.add(new Contact(user, partsContact[0].trim(), partsContact[1].substring(0, partsContact[1].length()-1)));
                        break;
                    default:
                        // substring max. 100 characters => restriction of column size on database
                        contactsSet.add(new Contact(user, "undefined format", contact.length()<100?contact:contact.substring(0, 99))); 
                        break;
                }
            }
        });
        return contactsSet;
    }
    
    public static boolean saveAttachment(Email email) {
        boolean result = false;
        if( ! email.getAttachments().isEmpty()){
            Path userDir = Paths.get(PathToFile.MAIN_REPOSITORY.getPath(), email.getUser().getId().toString(), email.getId().toString());  
            try {
                if( ! createDirectory(userDir)){ 
                    throw new Exception("It couldn't create a required directories.");
                }
                Iterator<Attachment> iter = email.getAttachments().iterator();
                while(iter.hasNext()){
                    Attachment attachment = iter.next();
//                    System.out.println("attachment="+attachment);
                    Files.write(Paths.get(userDir.toString(), attachment.getName()), attachment.getContent());
                }
                result = true;
            } catch (Exception ex) { 
                throw new RuntimeException("The attachment couldn't be saved. Info: "+ex.getMessage());
            }
        }
        return result;
    }
    
    private static boolean createDirectory(Path path) throws RuntimeException{
        try{
            File pathToFile = new File(path.toUri());
            if( ! pathToFile.isDirectory())
                pathToFile.mkdirs();
        }catch(Exception ex){
            return false;
        }
        return true;
    }

}
