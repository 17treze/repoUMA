package it.tndigitale.a4g.proxy.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.JAXBElement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Configuration
public class JacksonConfiguration {

	@Bean
	public ObjectMapper objectMapper() {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
		mapper.addMixIn(JAXBElement.class, JAXBElementMixIn.class);
		JavaTimeModule timeModule = new JavaTimeModule();
		    timeModule.addDeserializer(LocalDate.class,
		            new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		    timeModule.addDeserializer(LocalDateTime.class,
		            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		    timeModule.addSerializer(LocalDate.class,
		            new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		    timeModule.addSerializer(LocalDateTime.class,
		            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		mapper.registerModule(timeModule);
		return mapper;
	
	}
}