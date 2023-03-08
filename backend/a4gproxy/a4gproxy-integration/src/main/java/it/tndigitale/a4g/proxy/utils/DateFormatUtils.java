/**
 * 
 */
package it.tndigitale.a4g.proxy.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateFormatUtils {

	private static final Logger log = LoggerFactory.getLogger(DateFormatUtils.class);

	public static final String DATE_FORMAT_PARIX = "yyyyMMdd";
	public static final String DATE_FORMAT_ANAGRAFICA = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String IO_ITALIA = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String DATE_FORMAT_AMF = "dd/MM/yyyy";
	public static final String DATE_FORMAT_BDN_ANAGRAFICA_ALLEVAMENTI = "yyyy-MM-dd";
	private static DateFormat formatterParix=new SimpleDateFormat(DATE_FORMAT_PARIX);
	private static DateFormat formatterAnagrafeTributaria = new SimpleDateFormat(DATE_FORMAT_ANAGRAFICA);
	private static DateFormat formatterOut = new SimpleDateFormat(DATE_FORMAT_AMF);

	private DateFormatUtils() {
	}

	/*
	 * converte una data Parix in una AMF
	 * @param String data in formato Parix
	 * @return String data in formato AMF
	 */
	public static String convertiDataParix(String stringData) {
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

	public static LocalDateTime convertiDataParixLocalDateTime(String stringData) {
		if (stringData == null) {
			return null;
		}
		try {
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PARIX);
			LocalDateTime time = LocalDate.parse(stringData, formatter).atStartOfDay();
			return time;
		} catch (Exception e) {
			log.error("Fallita conversione data PARIX ".concat(stringData), e);
			return null;
		}
	}

	public static LocalDate convertiDataParixLocalDate(String stringData) {
		if (stringData == null)
			return null;
		return convertiDataParixLocalDateTime(stringData).toLocalDate();
	}

	/*
	 * converte una data Anagrafe Tributaria in una AMF
	 * @param String data in formato AnagrafeTributaria
	 * @return String data in formato AMF
	 */
	public static String convertiDataAnagrafeTributaria(String stringData) {
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

	/**
	 * @param stringData= stringa contente la data in 3 formati possibili
	 * 			"dataNascita": "-1005264000000",
	 * 			"dataInizio": "08/05/1964",						--DATE_FORMAT_AMF
	 * 			"dtProtocollazione": "2018-11-23T22:17:56.000+0000", --DATE_FORMAT_ANAGRAFICA
	 * @return
	 * @throws Exception
	 */
	public static Date convertiDataDichiarazione(String stringData) {
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

	public static String convertiDataAnagrafeTributaria(Long dateMillisecond) {
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

	/*
	 * converte una data JAXBElement<XMLGregorianCalendar> in LocalDateTime
	 */
	public static LocalDateTime convertiDataATfromGregorian (JAXBElement<XMLGregorianCalendar> data) {
		if (data == null) {
			return null;
		}
		try {
			return convertiDataATfromGregorian(data.getValue());
		} catch (Exception e) {
			log.error("Conversione data fallita", e);
			return null;
		}
	}

	/*
	 * converte una data JAXBElement<XMLGregorianCalendar> in LocalDate
	 */
	public static LocalDate convertiLocalDate(JAXBElement<XMLGregorianCalendar> data) {
		if (data == null) {
			return null;
		}
		try {
			return convertiDataATfromGregorian(data).toLocalDate();
		} catch (Exception e) {
			log.error("Conversione data fallita", e);
			return null;
		}
	}

	/*
	 * converte una data XMLGregorianCalendar in LocalDateTime
	 */
	public static LocalDateTime convertiDataATfromGregorian(XMLGregorianCalendar data) {
		if (data == null) {
			return null;
		}
		try {
			return data.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
		} catch (Exception e) {
			log.error("Conversione data fallita", e);
			return null;
		}
	}

	/*
	 * converte una data XMLGregorianCalendar in LocalDate
	 */
	public static LocalDate convertiLocalDate(XMLGregorianCalendar data) {
		if (data == null) {
			return null;
		}
		try {
			return convertiDataATfromGregorian(data).toLocalDate();
		} catch (Exception e) {
			log.error("Conversione data fallita", e);
			return null;
		}
	}
	/*
	 * converte una data LocalDateTime in String con formattazione IO_ITALIA
	 */
	public static String localDateTimeToStringIoItaliaPattern(LocalDateTime localDateTime) {
		DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern(IO_ITALIA);
		return localDateTime.format(simpleDateFormat);
	}
	
	public static LocalDate convertiDataBdnAnagraficaAllevamentiLocalDate(String stringData) {
		if (stringData == null)
			return null;
		return convertiDataBdnAnagraficaAllevamentiLocalDateTime(stringData).toLocalDate();
	}
	
	public static LocalDateTime convertiDataBdnAnagraficaAllevamentiLocalDateTime(String stringData) {
		if (stringData == null) {
			return null;
		}
		try {
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_BDN_ANAGRAFICA_ALLEVAMENTI);
			LocalDateTime time = LocalDate.parse(stringData, formatter).atStartOfDay();
			return time;
		} catch (Exception e) {
			log.error("Fallita conversione data BDN Anagrafica Allevamenti ".concat(stringData), e);
			return null;
		}
	}
}
