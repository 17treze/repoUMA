package it.tndigitale.a4g.proxy.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * 
 * Serializer da utilizzare per gli oggetti passati per le chiamata con il client Swagger.
 * Purtroppo il formato delle date sono state sovrascritte dalla classe JacksonConfiguration. 
 * @author s.caccia
 *
 */
public class LocalDateTimeSerializerDefault extends StdSerializer<LocalDateTime>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4470473824814198394L;
	
	public LocalDateTimeSerializerDefault(){
		this(null);
	}

	public LocalDateTimeSerializerDefault(Class<LocalDateTime> t) {
		super(t);
	}
	
	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        gen.writeString(value.format(dtf));
	}
	



}
