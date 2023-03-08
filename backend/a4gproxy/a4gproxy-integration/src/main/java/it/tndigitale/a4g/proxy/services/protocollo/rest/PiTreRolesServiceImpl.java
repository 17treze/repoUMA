package it.tndigitale.a4g.proxy.services.protocollo.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.protocollo.client.model.GetRoleResponse;

@Service
public class PiTreRolesServiceImpl extends PiTreBaseService {

	private static final Logger log = LoggerFactory.getLogger(PiTreRolesServiceImpl.class);

	public GetRoleResponse getRole(String codeRole) {
		log.debug("PiTre getRole");
		return restApi4Ptre.getRole(codeRole, null);
	}
}
