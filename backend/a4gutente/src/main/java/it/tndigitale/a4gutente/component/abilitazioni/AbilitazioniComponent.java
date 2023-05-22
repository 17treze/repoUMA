package it.tndigitale.a4gutente.component.abilitazioni;

import static it.tndigitale.a4gutente.codici.Ruoli.CREA_UTENTE;
import static it.tndigitale.a4gutente.codici.Ruoli.EDITA_DOMANDE;
import static it.tndigitale.a4gutente.codici.Ruoli.EDITA_ISTRUTTORIA_DOMANDA;
import static it.tndigitale.a4gutente.codici.Ruoli.EDITA_PROPRI_DATI_PERSONALI;
import static it.tndigitale.a4gutente.codici.Ruoli.EDITA_TUTTI_I_DATI_PERSONALI;
import static it.tndigitale.a4gutente.codici.Ruoli.IMPORTAZIONE_MASSIVA_UTENTI;
import static it.tndigitale.a4gutente.codici.Ruoli.VISUALIZZA_DOMANDE;
import static it.tndigitale.a4gutente.codici.Ruoli.VISUALIZZA_ISTRUTTORIA_DOMANDA;
import static it.tndigitale.a4gutente.codici.Ruoli.VISUALIZZA_PROFILI_UTENTE;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;

@Component("abilitazioni")
public class AbilitazioniComponent {
	
	private static final Logger logger = LoggerFactory.getLogger(AbilitazioniComponent.class);
	
	@Value("${wso2Host}")
	private String wso2Host;
	
	private UtenteComponent utenteComponent;
	
	@Autowired
	public AbilitazioniComponent setUtenteComponent(UtenteComponent utenteComponent) {
		this.utenteComponent = utenteComponent;
		return this;
	}
	
	public boolean checkImportazioneMassivaUtenti() {
		return utenteComponent.haUnRuolo(IMPORTAZIONE_MASSIVA_UTENTI);
	}
	
	public boolean checkCreaUtente() {
		return utenteComponent.haUnRuolo(CREA_UTENTE);
	}
	
	public boolean checkVisualizzaDomande() {
		return utenteComponent.haUnRuolo(VISUALIZZA_DOMANDE);
	}
	
	public boolean checkEditaPropriDatiPersonali() {
		return utenteComponent.haUnRuolo(EDITA_PROPRI_DATI_PERSONALI);
	}
	
	public boolean checkEditaDatiTuttePersone() {
		return utenteComponent.haUnRuolo(EDITA_TUTTI_I_DATI_PERSONALI);
	}
	
	public boolean checkEditaDomande() {
		return utenteComponent.haUnRuolo(EDITA_DOMANDE);
	}
	
	public boolean checkVisualizzaIstruttoriaDomanda() {
		return utenteComponent.haUnRuolo(VISUALIZZA_ISTRUTTORIA_DOMANDA);
	}
	
	public boolean checkEditaIstruttoriaDomanda() {
		return utenteComponent.haUnRuolo(EDITA_ISTRUTTORIA_DOMANDA);
	}
	
	public boolean checkVisualizzaProfiliUtente() {
		return utenteComponent.haUnRuolo(VISUALIZZA_PROFILI_UTENTE);
	}
	
	/**
	 * <p>
	 * Verifica se l'access-token ricevuto da wso2 risulta valido
	 * </p>
	 *
	 * @param request
	 */
	public String getWso2Username(String accessToken) {
		
		try {
			StringBuilder command = new StringBuilder(
					"curl -k -u admin:admin -H 'Content-Type: application/x-www-form-urlencoded' -X POST --data token=");
			command.append(URLEncoder.encode(accessToken, StandardCharsets.UTF_8.toString()));
			command.append(" https://");
			command.append(wso2Host);
			command.append(":9443/oauth2/introspect");
			
			Process process = Runtime.getRuntime().exec(command.toString());
			
			InputStream input = process.getInputStream();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			int nRead;
			byte[] data = new byte[4];
			while ((nRead = input.readNBytes(data, 0, data.length)) != 0) {
				output.write(data, 0, nRead);
			}
			output.flush();
			String strResponse = new String(output.toByteArray());
			logger.info("oauth2Response: " + strResponse);
			
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> oauth2Response = mapper.readValue(strResponse, Map.class);
			
			Boolean active = (Boolean) oauth2Response.get("active");
			
			if (active) {
				return (String) oauth2Response.get("username");
			}
			return null;
		}
		catch (Exception e) {
			logger.error("Exception:", e);
			return null;
		}
	}
}
