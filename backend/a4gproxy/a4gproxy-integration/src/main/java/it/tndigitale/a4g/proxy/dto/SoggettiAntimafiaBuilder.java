package it.tndigitale.a4g.proxy.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4g.proxy.config.DateFormatConfig;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabasgcaTab;



@Component
public class SoggettiAntimafiaBuilder {
	@Autowired
	private DateFormatConfig dateFormat;
	
	public List<AabasgcaTab> build(JsonNode dichiarazione, boolean skipSelezionato) {
		List<AabasgcaTab> soggettiAntimafia=new ArrayList<>();
		JsonNode soggettiArray=dichiarazione.path("dettaglioImpresa").path("soggettiImpresa");
		for (JsonNode soggetto : soggettiArray) {
			JsonNode caricheArray=soggetto.path("carica");
			for(JsonNode carica : caricheArray) {
				//skipSelezionato è utilizzato per i dettagli delle imprese collegate per fare in modo di salvare tutti i soggetti
				if (carica.path("selezionato").asBoolean() || skipSelezionato) {
					AabasgcaTab soggettoAntimafia=new AabasgcaTab();
					soggettoAntimafia.setFlagVisura(BigDecimal.ZERO);
					soggettoAntimafia.setCuaa(soggetto.path("codiceFiscale").textValue());	
					soggettoAntimafia.setCodiCari(carica.path("codice").textValue());
					soggettoAntimafia.setDataInizVali(dateFormat.convertiDataDichiarazione(carica.path("dataInizio").textValue()));
					//versione 1.6
					soggettoAntimafia.setDescCogn(soggetto.path("cognome").textValue());
					soggettoAntimafia.setDescNome(soggetto.path("nome").textValue());
					soggettoAntimafia.setCodiSex(soggetto.path("sesso").textValue());
					soggettoAntimafia.setDataNasc(dateFormat.convertiDataDichiarazione(soggetto.path("dataNascita").textValue()));
					soggettoAntimafia.setCodiNatuGiur("PF");
					soggettoAntimafia.setCodiSiglProvNasc(soggetto.path("provinciaNascita").textValue());
					soggettoAntimafia.setDescComuNasc(soggetto.path("comuneNascita").textValue());
					soggettoAntimafia.setCodiSiglProvReca(soggetto.path("provinciaResidenza").textValue());
					soggettoAntimafia.setDescComuReca(soggetto.path("comuneResidenza").textValue());
					soggettoAntimafia.setCodiCappReca(soggetto.path("capResidenza").textValue());
					Indirizzo indirizzo=EstrapolaIndirizzo.estrapolaIndirizzo(soggetto.path("indirizzoResidenza").textValue());
					soggettoAntimafia.setDescIndiReca(indirizzo.getIndirizzo());
					soggettoAntimafia.setNumeCiviReca(indirizzo.getNumeroCivico());
					soggettiAntimafia.add(soggettoAntimafia);
				}
			}
		}
		return soggettiAntimafia;
	}

}
