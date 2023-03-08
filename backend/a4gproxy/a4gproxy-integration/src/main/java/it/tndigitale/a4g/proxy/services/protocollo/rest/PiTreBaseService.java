package it.tndigitale.a4g.proxy.services.protocollo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import it.tndigitale.a4g.proxy.services.protocollo.client.PitreClient;


public class PiTreBaseService {
	@Value("${pitre.username}")
	protected String wsPiTreUsername;

	@Value("${pitre.codeadm}")
	protected String wsPiTreCodeAdm;

	// Identificativo del sistema informativo che contatta PITRE
	@Value("${pitre.codeapplication:A4G}")
	protected String wsPiTreCodeApplication;

	// Ruolo del sistema A4G in PITRE
	@Value("${pitre.coderolelogin:RDA_A4G}")
	protected String wsPiTreCodeRoleLogin;

	// Istanza di PITRE
	@Value("${pitre.codedefaultregister:PAT}")
	protected String wsPiTreCodeDefaultRegister;
	
	@Value("${pitre.s151.coderf:RFS151}")
	protected String codeRF;
	
	@Autowired
	protected PitreClient restApi4Ptre;
}
