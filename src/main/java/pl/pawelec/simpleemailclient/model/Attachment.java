/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author mirek
 */
public class Attachment{
    private String name;
    private byte[] content;
    private String mimeType;
    private Long size;

    public Attachment() {
    }

    public Attachment(String fileName, byte[] fileContent) {
        this.name = fileName;
        this.content = fileContent;
    }

    public Attachment(String name, byte[] content, String mimeType, Long size) {
        this.name = name;
        this.content = content;
        this.mimeType = mimeType;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Arrays.hashCode(this.content);
        hash = 97 * hash + Objects.hashCode(this.mimeType);
        hash = 97 * hash + Objects.hashCode(this.size);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Attachment other = (Attachment) obj;
        if (!Objects.equals(this.name, other.name)) return false;
        if (!Arrays.equals(this.content, other.content)) return false;
        if (!Objects.equals(this.mimeType, other.mimeType)) return false;
        if (!Objects.equals(this.size, other.size)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Attachment{" 
                + "name=" + name 
                + ", isContent=" + (content!=null) 
                + ", mimeType=" + mimeType 
                + ", size=" + size 
                + '}';
    }

}
