/**
 * 
 */
package it.tndigitale.a4g.proxy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe di utilità che converte le date dei servizi esterni in date A4G
 * 
 * @author B.Conetta
 *
 */
@Configuration
@Deprecated
public class DateFormatConfig {

	private static final Logger log = LoggerFactory.getLogger(DateFormatConfig.class);

	public static final String DATE_FORMAT_ANAGRAFICA = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String DATE_FORMAT_AMF = "dd/MM/yyyy";
	private DateFormat formatterDichiarazioneAntimafia;
	private DateFormat formatterOut;

	@PostConstruct
	private void buildFormatters() {
		formatterDichiarazioneAntimafia = new SimpleDateFormat(DATE_FORMAT_ANAGRAFICA);
		formatterOut = new SimpleDateFormat(DATE_FORMAT_AMF);
	}
	
	/**
	 * @param stringData= stringa contente la data in 3 formati possibili
	 * 			"dataNascita": "-1005264000000",
	 * 			"dataInizio": "08/05/1964",						--DATE_FORMAT_AMF
	 * 			"dtProtocollazione": "2018-11-23T22:17:56.000+0000", --DATE_FORMAT_ANAGRAFICA
	 * @return
	 * @throws Exception
	 */
	public Date convertiDataDichiarazione(String stringData) {
		if (stringData == null) {
			return null;
		}
		log.debug("Tentativo di conversione data: ".concat(stringData));
		try {
			return formatterDichiarazioneAntimafia.parse(stringData);
		} catch (Exception e) {
			log.debug("Tentativo fallito di conversione data utilizzando formato ".concat(DATE_FORMAT_ANAGRAFICA));
			try {
				return formatterOut.parse(stringData);
			} catch (Exception e1) {
				log.debug("Tentativo fallito di conversione data utilizzando formato ".concat(DATE_FORMAT_AMF));
				try {
					return new Date(Long.valueOf(stringData));
				} catch (Exception e2) {
					log.error("Tentativi di conversione data falliti",e2);
					throw new IllegalArgumentException("Tentativi di conversione data falliti",e2);
				}
			}
		}
	}
}
