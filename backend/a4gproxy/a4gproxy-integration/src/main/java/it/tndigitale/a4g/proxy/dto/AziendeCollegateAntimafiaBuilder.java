package it.tndigitale.a4g.proxy.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4g.proxy.config.DateFormatConfig;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaazcoTab;


@Component
public class AziendeCollegateAntimafiaBuilder {
	@Autowired
	private DateFormatConfig dateFormat;
	
	public List<AabaazcoTab> build(JsonNode dichiarazione) {
		List<AabaazcoTab> aziendeAntimafia=new ArrayList<AabaazcoTab>();
		JsonNode aziendeCollegateArray=dichiarazione.path("dettaglioImpresa").path("aziendeCollegate");
		for (JsonNode azienda : aziendeCollegateArray) {
			AabaazcoTab aziendaAntimafia=new AabaazcoTab();
			aziendaAntimafia.setCuaa(azienda.path("codiceFiscale").textValue());	
			aziendaAntimafia.setFlagVisura(BigDecimal.ZERO);
			JsonNode carica=azienda.path("carica").get(0);
			aziendaAntimafia.setCodiCari(carica.path("codice").textValue());
			aziendaAntimafia.setDataInizCari(dateFormat.convertiDataDichiarazione(carica.path("dataInizio").textValue()));
			aziendeAntimafia.add(aziendaAntimafia);
		}
		return aziendeAntimafia;
	}

}
