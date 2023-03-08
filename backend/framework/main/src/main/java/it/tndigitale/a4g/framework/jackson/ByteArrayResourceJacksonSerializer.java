package it.tndigitale.a4g.framework.jackson;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * 
 * @author ite3279
 * Classe per la gestione della serializzazione di ByteArrayResource. 
 */
public class ByteArrayResourceJacksonSerializer extends StdSerializer<ByteArrayResource> {
	public ByteArrayResourceJacksonSerializer() {
        this(null);
    }
  
    public ByteArrayResourceJacksonSerializer(Class<ByteArrayResource> t) {
        super(t);
    }
 
    @Override
    public void serialize(
    		ByteArrayResource value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
 
        jgen.writeStartObject();
        jgen.writeBinaryField(Fields.BYTEARRAY_RESOURCE_BYTEARRAY.getField(),value.getByteArray());
        jgen.writeStringField(Fields.BYTEARRAY_RESOURCE_DESCRIPTION.getField(), value.getDescription());
        jgen.writeStringField(Fields.BYTEARRAY_RESOURCE_FILENAME.getField(), value.getFilename());
        jgen.writeEndObject();
    }

    @Override
    public void serializeWithType(ByteArrayResource value, JsonGenerator jgen, SerializerProvider serializers,
            TypeSerializer typeSer) throws IOException {
    	serialize(value,jgen,serializers);
    }
}
