package it.tndigitale.a4g.proxy.dto;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4g.proxy.config.DateFormatConfig;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaantiTab;


@Component
public class DichiarazioneAntimafiaBuilder {
	
	@Autowired
	private DateFormatConfig dateFormat;
	
	
	public AabaantiTab build(JsonNode dichiarazione) {
		AabaantiTab dichAntimafia=new AabaantiTab();
		dichAntimafia.setIdAnti(dichiarazione.path("id").asLong());
		dichAntimafia.setCuaa(dichiarazione.path("azienda").path("cuaa").textValue());
		String formaGiuridicaCodice=dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("formaGiuridicaCodice").textValue();
		//0 = persona giuridica, 1 = persona fisica , 2 = ditta individuale
		if ("DI".equalsIgnoreCase(formaGiuridicaCodice)) 	dichAntimafia.setTipoPers(new BigDecimal(2));
		else 												dichAntimafia.setTipoPers(new BigDecimal(0));
		dichAntimafia.setDataInizVali(dateFormat.convertiDataDichiarazione(dichiarazione.path("dtProtocollazione").textValue()));
		dichAntimafia.setDataInviPref(dichAntimafia.getDataInizVali());
		return dichAntimafia;
	}


}
