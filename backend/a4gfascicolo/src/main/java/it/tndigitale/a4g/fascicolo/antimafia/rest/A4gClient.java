package it.tndigitale.a4g.fascicolo.antimafia.rest;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.antimafia.A4gfascicoloConstants;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@Component
public class A4gClient {

	private UtenteComponent utenteComponent;

	protected HttpHeaders createHeaders() {
		String username = utenteComponent.utenza();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (isADDAuthentication(username)) {
			headers.set(A4gfascicoloConstants.HEADER_UPN, username);
		} else {
			headers.set(A4gfascicoloConstants.HEADER_CF, username);
		}
		return headers;		
	}

	protected boolean isADDAuthentication(String username) {
		return username.indexOf("@") > 0;
	}
	
	
	@Autowired
	public void setUtenteComponent(UtenteComponent utenteComponent) {
		this.utenteComponent = utenteComponent;
	}
	
}
