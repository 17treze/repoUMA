package it.tndigitale.a4g.fascicolo.antimafia.config;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.antimafia.dto.KeyValueStringString;
import it.tndigitale.a4g.fascicolo.antimafia.dto.consultazione.Fascicolo;
import it.tndigitale.a4g.fascicolo.antimafia.exceptions.UtenteException;
import it.tndigitale.a4g.fascicolo.antimafia.service.ConsultazioneService;
import it.tndigitale.a4g.fascicolo.antimafia.service.ConsultazioneServiceImpl;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@Component
public class FascicoloPermissionEvaluator implements PermissionEvaluator {
	
	private static Logger logger = LoggerFactory.getLogger(ConsultazioneServiceImpl.class);

	@Autowired
	private ConsultazioneService consultazioneService;
	
	@Autowired
	private UtenteComponent utenteComp;
	
	public static enum TipoControllo {
		ADMIN, ENTE, TITOLARE;
	}
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if (targetDomainObject == null) {
			return true;
		}
		if (!(targetDomainObject instanceof Fascicolo)) {
			return false;
		}
		if (permission == null) {
			return false;
		}
		Fascicolo fascicolo = (Fascicolo)targetDomainObject;
		TipoControllo tipoControllo = TipoControllo.valueOf(permission.toString().toUpperCase());
		if (TipoControllo.ADMIN.equals(tipoControllo)) {
			return checkAdmin(authentication, fascicolo);
		}
		if (TipoControllo.ENTE.equals(tipoControllo)) {
			return checkEnte(authentication, fascicolo);
		}
		if (TipoControllo.TITOLARE.equals(tipoControllo)) {
			return checkTitolare(authentication, fascicolo);
		}
		return false;
	}
	
	protected boolean checkAdmin(Authentication authentication, Fascicolo fascicolo) {
		return true;
	}
	protected boolean checkEnte(Authentication authentication, Fascicolo fascicolo) {
		try {
			List<Fascicolo> fascicoliEnti = consultazioneService.getFascicoliEnti("{\"cuaa\": \"" + fascicolo.getCuaa() + "\"}");
			return fascicoliEnti != null && fascicoliEnti.contains(fascicolo);
		} catch (Exception e) {
			logger.debug("Errore verificando l'abilitazione", e);
			return false;
		}
	}

	protected boolean checkTitolare(Authentication authentication, Fascicolo fascicolo) {
		try {
			KeyValueStringString aziendaTitolare = consultazioneService.getAziendaPersonaRappresentante(utenteComp.utenza(), fascicolo.getCuaa());
			return fascicolo.getCuaa() != null && fascicolo.getCuaa().equals(aziendaTitolare.getMkey());
		} catch (UtenteException ue) {
			logger.debug("Utente non abilitato: " + ue.getMessage());
			return false;
		} catch (Exception e) {
			logger.debug("Errore verificando l'abilitazione", e);
			return false;
		}
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		// TODO Auto-generated method stub
		return false;
	}

}
