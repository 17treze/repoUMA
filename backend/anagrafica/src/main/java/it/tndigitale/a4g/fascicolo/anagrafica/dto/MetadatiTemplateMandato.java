package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.util.EnumSet;

public enum MetadatiTemplateMandato {
	// Nr repertorio non deve essere usato per il mandato
	NR_REPERTORIO("Nr. repertorio"),
	CAA("CAA di riferimento"),
	CUAA("CUAA"),
	TIPO_DOCUMENTO("Tipo documento");
	
	private String originalFieldName;
	
	MetadatiTemplateMandato(final String originalFieldName) {
		this.originalFieldName = originalFieldName;
    }

	public String getOriginalFieldName() {
		return originalFieldName;
	}
	
    public static MetadatiTemplateMandato fromString(String text) {
    	   return EnumSet.allOf(MetadatiTemplateMandato.class)
    			   	.stream()
    		        .filter(e -> e.getOriginalFieldName().equals(text))
    		        .findAny()
    		        .orElseThrow(() -> new IllegalStateException(String.format("Unsupported type %s.", text)));
    }

}
