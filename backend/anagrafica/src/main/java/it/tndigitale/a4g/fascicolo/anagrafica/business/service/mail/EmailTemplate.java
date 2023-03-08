package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail;

public interface EmailTemplate {
	
	public static final String PREFISSO_NOME_QUALIFICATORE = "EMAIL_TEMPLATE_";

	/**
	 * Manda una mail con indirizzo destinatario definito nelle properties.
	 * 
	 * @param templateArgs usate per formattare il testo del messaggio
	 */
	void sendMail(String[] templateArgs);
	
	
	/**
	 * Manda una mail con indirizzo destinatario passato in input.
	 * 
	 * @param address - indirizzo mail destinatario
	 * @param templateArgs usate per formattare il testo del messaggio
	 */
	void sendMail(String address, String[] templateArgs);
	
	/**
	 * Manda una mail con indirizzo destinatario passato in input.
	 * 
	 * @param address - indirizzo mail destinatario
	 * @param oggettoArgs usate per formattare il testo del messaggio
	 * @param templateArgs usate per formattare il testo del messaggio
	 */
	default void sendMail(String address, String[] oggettoArgs, String[] templateArgs ) {
		
	};
	
	public static String getNomeQualificatore(String name) {
		return PREFISSO_NOME_QUALIFICATORE + name;
	}

}
