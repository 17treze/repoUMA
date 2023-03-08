package it.tndigitale.a4gutente.codici;

public enum TipoAutenticazione {
	
    SMARTCARD("urn:oasis:names:tc:SAML:2.0:ac:classes:Smartcard"),
    SECURITYCARD("urn:oasis:names:tc:SAML:2.0:ac:classes:SecureRemotePassword"),
    OTP("urn:oasis:names:tc:SAML:2.0:ac:classes:TimeSyncToken"),
    PASSWORD("urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport");
	
	
	private String shibAuthMethod;

	private TipoAutenticazione(String shibAuthMethod) {
		this.shibAuthMethod = shibAuthMethod;
	}
	
	public static TipoAutenticazione ofAuthenticationMethod(String shibAuthMethod) {
		for (TipoAutenticazione tipo : values()) {
			if (tipo.shibAuthMethod.equals(shibAuthMethod)) {
				return tipo;
			}
		}
		return null;
	}
}
