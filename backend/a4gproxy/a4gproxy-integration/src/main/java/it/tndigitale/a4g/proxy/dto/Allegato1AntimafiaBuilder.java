package it.tndigitale.a4g.proxy.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4g.proxy.config.DateFormatConfig;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Aabaall1Tab;



@Component
public class Allegato1AntimafiaBuilder {
	
	@Autowired
	private DateFormatConfig dateFormat;
	
	
	public Aabaall1Tab build(JsonNode dichiarazione)  {
		Aabaall1Tab all1Antimafia=new Aabaall1Tab();
		all1Antimafia.setDescImpr(dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("denominazione").textValue());
		all1Antimafia.setDescProvImpr(dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("estremiCCIAA").path("sede").textValue());
		all1Antimafia.setCodiNumeIscr(dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("estremiCCIAA").path("numeroIscrizione").textValue());
		all1Antimafia.setDataIscr(dateFormat.convertiDataDichiarazione(dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("estremiCCIAA").path("dataIscrizione").textValue()));
		String desc = dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("oggettoSociale").textValue();
		all1Antimafia.setDescOggeSoci(desc==null?null:desc.substring(0, Math.min(desc.length(), 3999)));
		all1Antimafia.setCodiFisc(dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("codiceFiscale").textValue());
		all1Antimafia.setCodiPartIvaa(dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("partitaIva").textValue());
		//all1Antimafia.setDescSedeLega(dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("sedeLegale").textValue());
		all1Antimafia.setDescPec(dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("indirizzoPEC").textValue());
		//versione 1.6
		all1Antimafia.setDescCogn(dichiarazione.path("datiDichiarazione").path("richiedente").path("cognome").textValue());
		all1Antimafia.setDescNome(dichiarazione.path("datiDichiarazione").path("richiedente").path("nome").textValue());
		all1Antimafia.setCodiSex(dichiarazione.path("datiDichiarazione").path("richiedente").path("sesso").textValue());
		all1Antimafia.setDataNasc(dateFormat.convertiDataDichiarazione(dichiarazione.path("datiDichiarazione").path("richiedente").path("dataNascita").textValue()));
		all1Antimafia.setCodiNatuGiur(dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("formaGiuridicaCodice").textValue());
		all1Antimafia.setCodiSiglProvNasc(dichiarazione.path("datiDichiarazione").path("richiedente").path("provinciaNascita").textValue());
		all1Antimafia.setDescComuNasc(dichiarazione.path("datiDichiarazione").path("richiedente").path("comuneNascita").textValue());
		all1Antimafia.setCodiSiglProvReca(dichiarazione.path("datiDichiarazione").path("richiedente").path("residenza").path("provincia").textValue());
		all1Antimafia.setDescComuReca(dichiarazione.path("datiDichiarazione").path("richiedente").path("residenza").path("comune").textValue());
		all1Antimafia.setCodiCappReca(dichiarazione.path("datiDichiarazione").path("richiedente").path("residenza").path("CAP").textValue());
		Indirizzo indirizzo=EstrapolaIndirizzo.estrapolaIndirizzo(dichiarazione.path("datiDichiarazione").path("richiedente").path("residenza").path("indirizzo").textValue());
		all1Antimafia.setDescIndiReca(indirizzo.getIndirizzo());
		all1Antimafia.setNumeCiviReca(indirizzo.getNumeroCivico());
		all1Antimafia.setCuaa(dichiarazione.path("azienda").path("cuaa").textValue());
		return all1Antimafia;
	}


}
