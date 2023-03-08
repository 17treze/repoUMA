package it.tndigitale.a4g.fascicolo.antimafia.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import it.tndigitale.a4g.fascicolo.antimafia.config.DateFormatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class AziendeCollegateBuilder {

	private static final Logger log = LoggerFactory.getLogger(DateFormatConfig.class);

	@Autowired
	DateFormatConfig dateFormatConfig;


	public ArrayList<AziendaCollegata> build(JsonNode personeSede) throws Exception {

		ArrayList<AziendaCollegata> aziendeCollegate = new ArrayList<>();
		Long totalePersoneSede = personeSede.path("totale").asLong();
		if (totalePersoneSede.compareTo(0L) > 0) {
			JsonNode personeSedeArray = personeSede.path("persona");
			
			for (JsonNode personaSede : personeSedeArray) {
				JsonNode personaGiuridicaSede = personaSede.path("personagiuridica");
				if (!personaGiuridicaSede.isNull()) {
					AziendaCollegata aziendaCollegata = new AziendaCollegata();
					aziendaCollegata.setCciaa(personaGiuridicaSede.path("cciaa").textValue());
					aziendaCollegata.setCodiceFiscale(personaGiuridicaSede.path("codicefiscale").textValue());
					aziendaCollegata.setDenominazione(personaGiuridicaSede.path("denominazione").textValue());
					aziendaCollegata.setProvinciaSede(personaGiuridicaSede.path("indirizzo").path("provincia").textValue());
					aziendaCollegata.setComuneSede(personaGiuridicaSede.path("indirizzo").path("comune").textValue());
					String toponimo = personaGiuridicaSede.path("indirizzo").path("toponimo").isNull() ? "" : personaGiuridicaSede.path("indirizzo").path("toponimo").textValue();
					String via = personaGiuridicaSede.path("indirizzo").path("toponimo").isNull() ? "" : personaGiuridicaSede.path("indirizzo").path("via").textValue();
					String nCivico = personaGiuridicaSede.path("indirizzo").path("ncivico").isNull() ? "" : personaGiuridicaSede.path("indirizzo").path("ncivico").textValue();
					
					String indirizzoCompleto = toponimo + " " + via;

					aziendaCollegata.setnCivico(nCivico);
					aziendaCollegata.setIndirizzoSede(indirizzoCompleto);
					aziendaCollegata.setCapSede(personaGiuridicaSede.path("indirizzo").path("cap").textValue());

					JsonNode carichePersonaFisicaElement = personaSede.path("cariche").path("carica");
					if (!carichePersonaFisicaElement.isNull()) {
						aziendaCollegata.setCarica(new ArrayList<>());
						for (JsonNode caricaIn : carichePersonaFisicaElement) {
							Carica carica = new Carica();
							carica.setCodice(caricaIn.path("ccarica").textValue());
							carica.setTipologia(caricaIn.path("descrizione").textValue());
							
							// se data fine esiste
							String dataFineCaricaString = dateFormatConfig.convertiDataParix(caricaIn.path("dtfine").textValue());
							carica.setDataInizio(dateFormatConfig.convertiDataParix(caricaIn.path("dtinizio").textValue()));
							carica.setDataFine(dataFineCaricaString);
							Date dataFineCarica;
							if (!dataFineCaricaString.isEmpty()) {
								DateFormat formatterAMF = new SimpleDateFormat(DateFormatConfig.DATE_FORMAT_AMF);
								dataFineCarica = formatterAMF.parse(dataFineCaricaString);
								Date now = new Date();
								// se la carica è scaduta
								if (dataFineCarica.compareTo(now) < 0) {
									break;
								}
							}
							aziendaCollegata.getCarica().add(carica);
						}
					} else {
						log.error(new StringBuilder("Errore nel recupero dei dati della carica della persona fisica ").append(personaSede.textValue()).toString());
						// TODO: devo lanciare una eccezione qui?
					}
					if (!aziendaCollegata.getCarica().isEmpty()) {
						aziendeCollegate.add(aziendaCollegata);
					}
					
				}
			}
		}
		return aziendeCollegate;
	}
}
