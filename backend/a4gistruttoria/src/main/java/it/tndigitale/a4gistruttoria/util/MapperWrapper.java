package it.tndigitale.a4gistruttoria.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MapperWrapper {

    @Autowired private ObjectMapper mapper;
    
	public String asJsonString(final Object obj) {
	    try {
	        return mapper.writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

    public <T> T readValue(String content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T readValue(String content, TypeReference valueTypeRef) {
        try {
            return (T) mapper.readValue(content, valueTypeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
