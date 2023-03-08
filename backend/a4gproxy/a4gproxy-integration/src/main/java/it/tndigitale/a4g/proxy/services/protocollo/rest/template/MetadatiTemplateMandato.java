package it.tndigitale.a4g.proxy.services.protocollo.rest.template;


enum MetadatiTemplateMandato implements IFieldNameList {
	CAA("CAA di riferimento"),
	CUAA("CUAA"),
	TIPO_DOCUMENTO("Tipo documento");
	
	private String originalFieldName;
	
	MetadatiTemplateMandato(final String originalFieldName) {
		this.originalFieldName = originalFieldName;
    }

    @Override
	public String getOriginalFieldName() {
		return originalFieldName;
	}

}
