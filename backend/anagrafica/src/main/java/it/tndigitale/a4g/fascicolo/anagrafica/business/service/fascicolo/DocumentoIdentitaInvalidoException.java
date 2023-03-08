package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

public class DocumentoIdentitaInvalidoException extends Exception {
	private static final long serialVersionUID = -6639975811021360536L;

	public DocumentoIdentitaInvalidoException() {
		super("Il documento non contiene tutte le informazioni compilate");
	}
	
	public DocumentoIdentitaInvalidoException(String msg) {
		super(msg);
	}
}
