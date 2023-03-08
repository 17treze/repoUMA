package it.tndigitale.a4g.fascicolo.antimafia.dto;

import it.tndigitale.a4g.fascicolo.antimafia.config.DateFormatConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class RichiedenteBuilder {

	private static final String DATI_NASCITA = "datiNascita";
	@Autowired
	private ResidenzaBuilder residenzaBuilder;
	@Autowired
	private DateFormatConfig dateFormatConfig;


	public Richiedente build(JsonNode personaFisica,JsonNode titolare) {
		Richiedente richiedente = new Richiedente();
		richiedente.setNome(personaFisica.path("nome").textValue());
		richiedente.setCognome(personaFisica.path("cognome").textValue());
		richiedente.setCodiceFiscale(personaFisica.path("codiceFiscale").textValue());
		
		richiedente.setComuneNascita(personaFisica.path(DATI_NASCITA).path("comune").textValue());
		richiedente.setProvinciaNascita(personaFisica.path(DATI_NASCITA).path("provincia").textValue());
		richiedente.setSesso(personaFisica.path("sesso").path("valore").textValue());
		richiedente.setIndirizzoPEC(personaFisica.path("indirizzo").path("indirizzopec").textValue());
		richiedente.setCarica(titolare.path("descrizione").textValue());
		richiedente.setResidenza(residenzaBuilder.build(personaFisica.path("domicilioFiscale").path("ubicazione")));
		richiedente.setDataNascita(dateFormatConfig.convertiDataAnagrafeTributaria(personaFisica.path(DATI_NASCITA).path("data").asLong()));
		richiedente.setDtInizioCarica(dateFormatConfig.convertiDataParix(titolare.path("dtinizio").textValue()));
		richiedente.setDtFineCarica(dateFormatConfig.convertiDataParix(titolare.path("dtfine").textValue()));
		return richiedente;
	}

}
