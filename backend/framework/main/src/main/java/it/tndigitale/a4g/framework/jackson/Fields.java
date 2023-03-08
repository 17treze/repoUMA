package it.tndigitale.a4g.framework.jackson;

public enum Fields 
{
    AUTHENTICATION_CREDENTIALS("credentials"), 
    AUTHENTICATION_AUTHORITIES("authorities"),
    AUTHENTICATION_AUTHORITY("authority"),
    AUTHENTICATION_NAME("name"), 
    AUTHENTICATION_DETAILS("details"),
    AUTHENTICATION_PRINCIPAL("principal"), 
    AUTHENTICATION_ISAUTHENTICATED("isAuthenticated"),
    BYTEARRAY_RESOURCE_BYTEARRAY("byteArray"),
    BYTEARRAY_RESOURCE_DESCRIPTION("description"),
    BYTEARRAY_RESOURCE_FILENAME("filename");
 
    private String field;
 
    Fields(String field) {
        this.field = field;
    }
 
    public String getField() {
        return field;
    }
}