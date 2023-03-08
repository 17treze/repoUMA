package it.tndigitale.a4g.framework.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;



/**
 * @author ite3279
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.ANY)
@JsonDeserialize(using = ByteArrayResourceJacksonDeserializer.class)
@JsonSerialize(using = ByteArrayResourceJacksonSerializer.class)
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
public abstract class ByteArrayResourceMixin {
    @JsonCreator
    public ByteArrayResourceMixin(
            @JsonProperty("byteArray") byte[] byteArray,
            @JsonProperty("description") String String) {
        System.out.println("Wont be called");
    }
    
    @JsonProperty("byteArray") abstract byte[] getByteArray();
    @JsonProperty("description") abstract String getDescription();
    @JsonProperty("filename") abstract String getFilename();
}
