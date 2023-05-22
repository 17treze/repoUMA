/**
 * 
 */
package it.tndigitale.a4gutente.config;

public abstract class Costanti {
	
	public static final String DEFAULT_ROLE_PREFIX = "ROLE_";
	
	public static final String HEADER_CF = "codicefiscale";
	public static final String HEADER_UPN = "upn";
	public static final String HEADER_NOME = "givenname";
	public static final String HEADER_COGNOME = "surname";
	public static final String HEADER_MAIL = "email";
	public static final String HEADER_SHIB_AUTH_INST = "Shib-Authentication-Instant";
	public static final String HEADER_SHIB_AUTH_METHOD = "Shib-Authentication-Method";
	public static final String HEADER_SPID_CODE = "pat_attribute_spidcode";
	public static final String HEADER_X509 = "x509base64";
	public static final Long CARICA_RAPPRESENTANTE = new Long(1);
	
	private Costanti() {
	}
	
}
