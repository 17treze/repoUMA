package it.tndigitale.a4gistruttoria.util;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.isEmpty;

public class JsonSupport {

    private static final Logger log = LoggerFactory.getLogger(JsonSupport.class);

    private static ObjectMapper mapper;

    private static ObjectWriter writer;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        reader = mapper.reader();
        writer = mapper.writer();
    }

    private static ObjectReader reader;

    public String serialize(Object target) {
        return toJson(target);
    }

    public String serialize(Object target, Class targetClass) {
        return toJson(target, targetClass);
    }

    public static String toJson(Object target){
        if (target == null) {
            return null;
        }
        return toJson(target, target.getClass());
    }

    public static String toJson(Object target, Class targetClass){
        if (target == null) {
            return null;
        }
        return executeTo(()-> mapper.writerFor(targetClass).writeValueAsString(target));
    }

    public static byte [] toByteJson(Object targer){
        if(targer == null){
            return null;
        }
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {

            writer.writeValue(bao, targer);
            bao.flush();
            bao.close();
        } catch (IOException e) {
            throw new RuntimeException(e) ;
        }

        return  bao.toByteArray();
    }

    public static <T> T toObject(String json,Class<T> targer){
        if(isEmpty(json)){
            return null;
        }

        return executeTo(()->  mapper.readValue(json, targer));
    }

    public static <T> T toObjectOfByte(byte [] json,Class<T> targer){
        if(json == null){
            return null;
        }

        return executeTo(()->  mapper.readValue(json, targer));
    }

    public static <T> List<T> toList(String json, Class<T[]> target){
        if(isEmpty(json)){
            return null;
        }

        return asList(toArray(json, target));
    }

    public static <T> List<T> toModificableList(String json, Class<T[]> target){
        if(isEmpty(json)){
            return null;
        }

        return Stream.of(toArray(json, target)).collect(Collectors.toList());
    }


    public static <T> T[] toArray(String json, Class<T[]> array){
        if(isEmpty(json)){
            return null;
        }
        return executeTo(()-> mapper.readValue(json,array));
    }

    static protected <T> T executeTo(Code<T> code){
        try {
            return code.get();
        } catch (Exception e) {
            log.error("There was an error translating to json", e);
            throw new RuntimeException();
        }
    }

   protected interface Code<T>{
       T get() throws Exception;
   }
}
