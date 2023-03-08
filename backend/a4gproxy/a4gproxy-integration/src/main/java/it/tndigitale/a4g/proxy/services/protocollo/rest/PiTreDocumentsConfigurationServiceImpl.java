package it.tndigitale.a4g.proxy.services.protocollo.rest;

import it.tndigitale.a4g.proxy.dto.protocollo.TipologiaDocumento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.proxy.services.protocollo.PiTreException;

@Service
public class PiTreDocumentsConfigurationServiceImpl {
	
	@Autowired
	private Environment env;

	private static Logger log = LoggerFactory.getLogger(PiTreDocumentsConfigurationServiceImpl.class);
	
	
	public String getRoleByDocumentType(TipologiaDocumento documentType) throws PiTreException {
		String role = env.getProperty("pitre." + documentType.name() + ".coderole");
		if (role == null || role.isEmpty()) {
			log.warn("Configurazione ruolo non trovata per tipo documento = {}", documentType.name());
			throw new PiTreException("Ruolo non trovato per il tipo di documento specificato. Verificare la configurazione.");
		}
		return role;
	}

	
	public String getProjectByDocumentType(TipologiaDocumento documentType) throws PiTreException {
		String project = env.getProperty("pitre." + documentType.name() + ".codeproject");
		if (project == null || project.isEmpty()) {
			log.warn("Configurazione progetto non trovata per tipo documento = {}", documentType.name());
			throw new PiTreException("Fascicolo non trovato per il tipo di documento specificato. Verificare la configurazione.");
		}
		return project;
	}
	
	public String getTemplateName(TipologiaDocumento documentType) {
		String templateName = env.getProperty("pitre." + documentType.name() + ".template-name");
		if (StringUtils.isEmpty(templateName)) {
			log.debug("Template non esistente per tipo documento = {}", documentType.name());
			return null;
		}
		return templateName;
	}
}
