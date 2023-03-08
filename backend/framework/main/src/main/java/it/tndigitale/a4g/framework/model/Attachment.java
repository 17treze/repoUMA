package it.tndigitale.a4g.framework.model;

import it.tndigitale.a4g.framework.support.StringSupport;

import java.util.Arrays;
import java.util.Objects;

public class Attachment {

    private String fileName;
    private byte[] file;

    public String getFileName() {
        return fileName;
    }

    public Attachment setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public byte[] getFile() {
        return file;
    }

    public Attachment setFile(byte[] file) {
        this.file = file;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(fileName, that.fileName) &&
                Arrays.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(fileName);
        result = 31 * result + Arrays.hashCode(file);
        return result;
    }

    public static Boolean isValid(Attachment attachment) {
        
        return (attachment != null &&
                attachment.getFile() != null &&
                !StringSupport.isEmptyOrNull(attachment.getFileName()));
    }
}
