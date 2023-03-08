package it.tndigitale.a4gutente.dto.csv;

import java.util.StringJoiner;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UtenteA4gMapperUtility {
	
	private static final String PROFILO_PAT = "DIPENDENTE_PAT";
	private static final String PROFILO_CAA = "DIPENDENTE_CAA";
	private static final String PROFILO_AZIENDA = "TITOLARE_AZIENDA_AGRICOLA";
	private static final String PROFILO_DISTRIBUTORE = "DIPENDENTE_DISTRIBUTORE";
	
	private static final String PAT = "Provincia Autonoma di Trento";
	private static final String AZIENDA = "Titolare o legale rappresentante di Azienda agricola";
	
	private static final String SI = "SI";
	private static final String NO = "NO";
	
	public String buildEnteApparteneza(String responsabilita,String caa,String denomDistributore, String comuneDistributore) {
		
		if(StringUtils.isEmpty(responsabilita)) {
			return "";
		}
		switch(responsabilita) {
			case PROFILO_PAT: return PAT;
			case PROFILO_CAA: return caa;
			case PROFILO_AZIENDA : return AZIENDA;
			case PROFILO_DISTRIBUTORE: return (!StringUtils.isEmpty(denomDistributore) && !StringUtils.isEmpty(comuneDistributore)) ? new StringJoiner(", ").add(denomDistributore).add(comuneDistributore).toString(): "";
			default: return "";	
		}
    }
	
	public static final String buildUtenteSospeso(Boolean utenteSospeso) {
		return utenteSospeso!= null && utenteSospeso ? SI : NO;
	}
	
	
}
