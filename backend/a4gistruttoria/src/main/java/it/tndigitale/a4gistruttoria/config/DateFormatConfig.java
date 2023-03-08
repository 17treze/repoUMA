/**
 * 
 */
package it.tndigitale.a4gistruttoria.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Classe di utilità che converte le date dei servizi esterni in date A4G
 * 
 * @author B.Conetta
 *
 */
@Configuration
public class DateFormatConfig {

	private static final Logger log = LoggerFactory.getLogger(DateFormatConfig.class);

	public static final String DATE_FORMAT_PARIX = "yyyyMMdd";
	public static final String DATE_FORMAT_ANAGRAFICA = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	public static final String DATE_FORMAT_AMF = "dd/MM/yyyy";
	private DateFormat formatterParix;
	private DateFormat formatterAnagrafeTributaria;
	private DateFormat formatterOut;

	@PostConstruct
	private void buildFormatters() {
		formatterParix = new SimpleDateFormat(DATE_FORMAT_PARIX);
		formatterAnagrafeTributaria = new SimpleDateFormat(DATE_FORMAT_ANAGRAFICA);
		formatterOut = new SimpleDateFormat(DATE_FORMAT_AMF);
	}

	/*
	 * converte una data Parix in una AMF
	 * @param String data in formato Parix
	 * @return String data in formato AMF
	 */
	public String convertiDataParix(String stringData) {
		if (stringData == null) {
			return "";
		}
		try {
			return formatterOut.format(formatterParix.parse(stringData));
		} catch (Exception e) {
			log.error("Fallita conversione data PARIX ".concat(stringData), e);
			return "";
		}
	}
	
	/*
	 * converte una data Anagrafe Tributaria in una AMF
	 * @param String data in formato AnagrafeTributaria
	 * @return String data in formato AMF
	 */
	public String convertiDataAnagrafeTributaria(String stringData) {
		if (stringData == null) {
			return "";
		}
		try {
			return formatterOut.format(formatterAnagrafeTributaria.parse(stringData));
		} catch (Exception e) {
			log.error("Fallita conversione data Anagrafe Tributaria ".concat(stringData), e);
			return "";
		}
	}
	
	public String convertiDataFull(Long dateMillisecond) {
		if (dateMillisecond == null) {
			return "";
		}
		try {
			return formatterAnagrafeTributaria.format(new Date(dateMillisecond));
		} catch (Exception e) {
			log.error("Fallita conversione data Anagrafe Tributaria ".concat(dateMillisecond.toString()), e);
			return "";
		}
	}
	
	public String convertiDataAnagrafeTributaria(Long dateMillisecond) {
		if (dateMillisecond == null) {
			return "";
		}
		try {
			return formatterOut.format(new Date(dateMillisecond));
		} catch (Exception e) {
			log.error("Fallita conversione data Anagrafe Tributaria ".concat(dateMillisecond.toString()), e);
			return "";
		}
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
			return formatterAnagrafeTributaria.parse(stringData);
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
	//2018-01-10T14:47:42.000Z
	public Date convertiJsonIsoDate(String stringData) {
		if (stringData == null) {
			return null;
		}
		try {
			DateFormat jsonDate = new SimpleDateFormat(JSON_DATE_FORMAT);
			return jsonDate.parse(stringData);
		} catch (Exception e) {
			log.error("Tentativi di conversione data falliti",e);
			throw new IllegalArgumentException("Tentativi di conversione data falliti",e);
		}
	}	
	
	public static LocalDateTime convertiToLocalDateTime(String stringData) {
		if (stringData == null) {
			return null;
		}
		try {
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_AMF);
			LocalDateTime time = LocalDate.parse(stringData, formatter).atStartOfDay();
			return time;
		} catch (Exception e) {
			log.error("Fallita conversione data ".concat(stringData), e);
			return null;
		}
	}
}
