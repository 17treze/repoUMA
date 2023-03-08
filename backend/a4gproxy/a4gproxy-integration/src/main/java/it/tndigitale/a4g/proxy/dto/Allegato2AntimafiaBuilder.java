package it.tndigitale.a4g.proxy.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4g.proxy.config.DateFormatConfig;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Aabaall2Tab;


@Component
public class Allegato2AntimafiaBuilder {
	
	@Autowired
	private DateFormatConfig dateFormat;
	
	
	public Aabaall2Tab build(JsonNode dichiarazione) {
		Aabaall2Tab all2Antimafia=new Aabaall2Tab();
		String ragioneSociale=dichiarazione.path("dettaglioImpresa").path("denominazione").textValue();
		ragioneSociale=StringUtils.isEmpty(ragioneSociale)?null:ragioneSociale.substring(0, Math.min(ragioneSociale.length(), 99));
		all2Antimafia.setDescImpr(ragioneSociale);
		all2Antimafia.setDescProvImpr(dichiarazione.path("dettaglioImpresa").path("estremiCCIAA").path("sede").textValue());
		all2Antimafia.setCodiNumeIscr(dichiarazione.path("dettaglioImpresa").path("estremiCCIAA").path("numeroIscrizione").textValue());
		all2Antimafia.setDataIscr(dateFormat.convertiDataDichiarazione(dichiarazione.path("dettaglioImpresa").path("estremiCCIAA").path("dataIscrizione").textValue()));
		all2Antimafia.setDescFormGiur(dichiarazione.path("dettaglioImpresa").path("formaGiuridicaDescrizione").textValue());
		all2Antimafia.setDescAttoCost(dichiarazione.path("dettaglioImpresa").path("estremiCCIAA").path("dataIscrizione").textValue());
		all2Antimafia.setDescCapiSoci(dichiarazione.path("dettaglioImpresa").path("dettaglioPersonaGiuridica").path("capitaleSociale").textValue());
		all2Antimafia.setDescDuraSoci(dichiarazione.path("dettaglioImpresa").path("dettaglioPersonaGiuridica").path("durata").textValue());
		String desc = dichiarazione.path("dettaglioImpresa").path("oggettoSociale").textValue();
		all2Antimafia.setDescOggeSoci(desc==null?null:desc.substring(0, Math.min(desc.length(), 3999)));
		all2Antimafia.setCodiFisc(dichiarazione.path("dettaglioImpresa").path("codiceFiscale").textValue());
		all2Antimafia.setCuaa(dichiarazione.path("dettaglioImpresa").path("codiceFiscale").textValue());
		//all2Antimafia.setDescSedeLega(dichiarazione.path("dettaglioImpresa").path("sedeLegale").textValue());
		all2Antimafia.setDescPec(dichiarazione.path("dettaglioImpresa").path("indirizzoPEC").textValue());
		//versione 1.6
		all2Antimafia.setDescRagiSoci(ragioneSociale);
		all2Antimafia.setCodiNatuGiur(dichiarazione.path("dettaglioImpresa").path("formaGiuridicaCodice").textValue());
		all2Antimafia.setCodiSiglProvReca(dichiarazione.path("dettaglioImpresa").path("estremiCCIAA").path("sede").textValue());//dichiarazione.path("dettaglioImpresa").path("sede").path("provincia").textValue());
		all2Antimafia.setDescComuReca(dichiarazione.path("dettaglioImpresa").path("sede").path("comune").textValue());
		all2Antimafia.setCodiCappReca(dichiarazione.path("dettaglioImpresa").path("sede").path("cap").textValue());
		StringBuilder indirizzo = new StringBuilder();
		indirizzo.append(dichiarazione.path("dettaglioImpresa").path("sede").path("toponimo").textValue()!=null ? dichiarazione.path("dettaglioImpresa").path("sede").path("toponimo").textValue().concat(" ") : "");
		indirizzo.append(dichiarazione.path("dettaglioImpresa").path("sede").path("via").textValue()!=null ? dichiarazione.path("dettaglioImpresa").path("sede").path("via").textValue().concat(" ") : "");
		indirizzo.append(dichiarazione.path("dettaglioImpresa").path("sede").path("frazione").textValue()!=null ? dichiarazione.path("dettaglioImpresa").path("sede").path("frazione").textValue().concat(" ") : "");
		all2Antimafia.setDescIndiReca(indirizzo.toString());
		all2Antimafia.setNumeCiviReca(dichiarazione.path("dettaglioImpresa").path("sede").path("ncivico").textValue());		
		return all2Antimafia;
	}


}
