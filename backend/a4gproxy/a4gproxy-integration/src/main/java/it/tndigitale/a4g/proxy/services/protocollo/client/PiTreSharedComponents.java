package it.tndigitale.a4g.proxy.services.protocollo.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.services.protocollo.rest.TokenServiceImpl;

@Component
class PiTreSharedComponents {

	private static final Logger log = LoggerFactory.getLogger(PiTreSharedComponents.class);
	
	@Autowired
	private TokenServiceImpl tokenService;
	
	private String authenticationToken;

	public String getAuthenticationToken() {
		if (authenticationToken == null)
			try {
				authenticationToken = tokenService.getToken();
			} catch (Exception e) {
				log.error("Fatal error getting authentication token for PiTre services.", e);
			}
		return authenticationToken;
	}

}
