package it.tndigitale.a4g.proxy.services.protocollo.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.protocollo.client.model.AuthenticateRequest;
import it.tndigitale.a4g.protocollo.client.model.AuthenticateResponse;
import it.tndigitale.a4g.proxy.services.protocollo.PiTreException;

@Service
public class PiTreTokenServiceImpl extends PiTreBaseService {

	private static final Logger log = LoggerFactory.getLogger(PiTreTokenServiceImpl.class);


	public String getAuthenticationToken() throws Exception {
		log.debug("PiTre GetAuthenticationToken");
		AuthenticateRequest request=new AuthenticateRequest();
		request.setCodeAdm(wsPiTreCodeAdm);
		request.setCodeApplication(wsPiTreCodeApplication);
		request.setCodeRole(wsPiTreCodeRoleLogin);
		request.setUsername(wsPiTreUsername);
		AuthenticateResponse authenticateResponse = restApi4Ptre.postAuthenticationTokenRoute(request);
		if (StringUtils.isEmpty(authenticateResponse.getToken())) {
			throw new PiTreException(PiTreException.ERROR_NO_TOKEN + ": " + authenticateResponse.getErrorMessage());
		}
		return authenticateResponse.getToken();
	}
}
