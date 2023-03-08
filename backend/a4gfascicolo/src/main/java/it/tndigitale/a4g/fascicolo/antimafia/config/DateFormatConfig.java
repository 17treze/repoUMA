/**
 * 
 */
package it.tndigitale.a4g.fascicolo.antimafia.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

	private static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	public static final String DATE_FORMAT_PARIX = "yyyyMMdd";
	public static final String DATE_FORMAT_ANAGRAFICA = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
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
}
