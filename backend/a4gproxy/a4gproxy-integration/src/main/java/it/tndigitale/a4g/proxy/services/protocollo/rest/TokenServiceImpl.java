package it.tndigitale.a4g.proxy.services.protocollo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl  {

	@Autowired
	private PiTreTokenServiceImpl piTreTokenService;

	public String getToken() throws Exception {
		return piTreTokenService.getAuthenticationToken();
	}
}
