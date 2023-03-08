package it.tndigitale.a4g.fascicolo.anagrafica.business.service.client;



import java.util.function.Supplier;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@Component
public class WebClientBuilderTn {
	public static final String HEADER_UPN = "upn";
	public static final String HEADER_CF = "codicefiscale";
	protected UtenteComponent utenteCorrente;
	
	public WebClient buildWith(Supplier<String> username) {
		Pair<String, String> headerUsername = buildUsernameHeader(username);
		if (headerUsername == null) {
			return WebClient.builder().build();
		} else {
			return WebClient.builder().defaultHeaders(headers -> headers.add(headerUsername.getFirst(), headerUsername.getSecond()))
					.build();
		}
	}
	
	private Pair<String, String> buildUsernameHeader(Supplier<String> username) {
			String currentUser = username.get();
			if (currentUser == null || currentUser.trim().length() == 0) {
				return null;
			}
			if (isADDAuthentication(currentUser)) {
				return Pair.of(HEADER_UPN, currentUser);
			} else {
				return Pair.of(HEADER_CF, currentUser);
			}
	}
	
	private boolean isADDAuthentication(String username) {
		return username.indexOf("@") > 0;
	}
}
