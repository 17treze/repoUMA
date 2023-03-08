package it.tndigitale.a4g.framework.jackson;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * 
 * @author ite3279
 * Classe per la gestione della deserializzazione di ByteArrayResource.
 */
public class ByteArrayResourceJacksonDeserializer extends StdDeserializer<ByteArrayResource> {
	public ByteArrayResourceJacksonDeserializer() {
		this(null);
	}

	public ByteArrayResourceJacksonDeserializer(Class<ByteArrayResource> t) {
		super(t);
	}

	@Override
	public ByteArrayResource deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		byte[] byteArray = ((TextNode) node.get(Fields.BYTEARRAY_RESOURCE_BYTEARRAY.getField())).binaryValue();

		String filename = node.get(Fields.BYTEARRAY_RESOURCE_FILENAME.getField()).asText();
		String description = node.get(Fields.BYTEARRAY_RESOURCE_DESCRIPTION.getField()).asText();

//      TODO [TEST-MODE]: save bytearray to verify converted content
//		try {
//
//			File file = new File("D:/" + filename); 
//			// Initialize a pointer
//			// in file using OutputStream
//			OutputStream os = new FileOutputStream(file);
//
//			// Starts writing the bytes in it
//			os.write(byteArray);
//			System.out.println("Successfully" + " byte inserted");
//
//			// Close the file
//			os.close();
//		} catch (Exception e) {
//			System.out.println("Exception: " + e);
//		}

		return new ByteArrayResource(byteArray, description) {
			@Override
			public String getFilename() {
				return filename;
			}
		};

	}

	@Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt,
            TypeDeserializer typeDeserializer)
        throws IOException{
    	return deserialize(p, ctxt);
    }
}
