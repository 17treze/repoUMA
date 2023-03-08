package it.tndigitale.a4gistruttoria.util;

import java.io.IOException;
import java.io.UncheckedIOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class CustomConverters {

	private CustomConverters() {
	}
	
	public static <T> T jsonConvert(String string, Class<T> dto) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(string, dto);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
