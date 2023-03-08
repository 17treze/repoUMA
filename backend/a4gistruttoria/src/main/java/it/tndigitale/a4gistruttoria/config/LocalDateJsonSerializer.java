package it.tndigitale.a4gistruttoria.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalDateJsonSerializer extends StdSerializer<LocalDate> {

	public LocalDateJsonSerializer() {
		this(null);
	}

	public LocalDateJsonSerializer(Class<LocalDate> t) {
		super(t);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6221797002658154235L;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 

	@Override
	public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		String stringValue = Optional.ofNullable(value).map(v -> formatter.format(v)).orElseGet(() -> "");
		gen.writeString(stringValue);
	}

}
