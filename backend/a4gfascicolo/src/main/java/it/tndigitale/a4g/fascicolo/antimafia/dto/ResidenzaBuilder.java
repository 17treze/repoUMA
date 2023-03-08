package it.tndigitale.a4g.fascicolo.antimafia.dto;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class ResidenzaBuilder {
	
 
    public Residenza build(JsonNode node) {
    	Residenza residenza = new Residenza();
    	residenza.setIndirizzo(node.path("toponimo").textValue());
    	residenza.setComune(node.path("comune").textValue());
    	residenza.setProvincia(node.path("provincia").textValue());
    	residenza.setCAP(node.path("cap").textValue());
    	
    	return residenza;
    }    
}
