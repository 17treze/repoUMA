package it.tndigitale.a4g.proxy.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;

import it.tndigitale.a4g.proxy.config.DateFormatConfig;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabafacoTab;


@Component
public class FamiliariAntimafiaBuilder {
	
	@Autowired
	private DateFormatConfig dateFormat;
	
	private JsonNode dichiarazione;
	private String codiceFiscale;
	
	final Map<String, BigDecimal> parentela = ImmutableMap.<String, BigDecimal>builder().
		      put("Figlio", 	BigDecimal.valueOf(7284l)).
		      put("Altro", 		BigDecimal.valueOf(7283l)).
		      put("Coniuge", 	BigDecimal.valueOf(7285l)).
		      build();
	
	public List<AabafacoTab> build() {
		List<AabafacoTab> familiariAntimafia=new ArrayList<>();
		JsonNode soggettiArray=dichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("soggettiImpresa");
		for (JsonNode soggetto : soggettiArray) {
			if (codiceFiscale.equalsIgnoreCase(soggetto.path("codiceFiscale").textValue())){
				JsonNode familiariArray=soggetto.path("familiariConviventi");
				for (JsonNode familiare : familiariArray) {
					AabafacoTab familiareAntimafia= new AabafacoTab();
					familiareAntimafia.setCuaa(familiare.path("codiceFiscale").textValue());
					familiareAntimafia.setDescCogn(familiare.path("cognome").textValue());
					familiareAntimafia.setDescNome(familiare.path("nome").textValue());
					familiareAntimafia.setDataNasc(dateFormat.convertiDataDichiarazione(familiare.path("dataNascita").textValue()));
					familiareAntimafia.setCodiSex(familiare.path("sesso").textValue());
					familiareAntimafia.setDescComuNasc(familiare.path("comuneNascita").textValue());
					familiareAntimafia.setCodiSiglProvNasc(familiare.path("provinciaNascita").textValue());
					Indirizzo indirizzo=EstrapolaIndirizzo.estrapolaIndirizzo(familiare.path("residenza").path("indirizzo").textValue());
					familiareAntimafia.setDescIndiStrd(indirizzo.getIndirizzo());
					familiareAntimafia.setDescGeogCivi(indirizzo.getNumeroCivico());
					familiareAntimafia.setCodiGeogCapp(familiare.path("residenza").path("CAP").textValue());
					familiareAntimafia.setCodiGeogSiglProv(familiare.path("residenza").path("provincia").textValue());
					familiareAntimafia.setDescGeogComu(familiare.path("residenza").path("comune").textValue());
					familiareAntimafia.setDecoTipoPare(parentela.get(familiare.path("gradoParentela").textValue()));
					//versione 1.6
					familiareAntimafia.setCodiNatuGiur("PF");
					familiareAntimafia.setCodiSiglProvReca(familiare.path("residenza").path("provincia").textValue());
					familiareAntimafia.setDescComuReca(familiare.path("residenza").path("comune").textValue());
					familiareAntimafia.setCodiCappReca(familiare.path("residenza").path("CAP").textValue());
					familiareAntimafia.setDescIndiReca(indirizzo.getIndirizzo());
					familiareAntimafia.setNumeCiviReca(indirizzo.getNumeroCivico());
					familiariAntimafia.add(familiareAntimafia);
				}
			}
		}
		return familiariAntimafia;
	}

	public FamiliariAntimafiaBuilder setDichiarazione(JsonNode dichiarazione) {
		this.dichiarazione = dichiarazione;
		return this;
	}


	public FamiliariAntimafiaBuilder setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}


}
