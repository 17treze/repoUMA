/**
 * 
 */
package it.tndigitale.a4g.proxy;

import java.time.LocalDate;
import java.util.Optional;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Classe per la conversione da usare in fase di generazione
 * da xml a java
 * 
 * @author it417
 *
 */
public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate> {

	@Override
	public LocalDate unmarshal(String v) throws Exception {
		return Optional.ofNullable(v).filter(s -> !s.isEmpty()).map(s -> LocalDate.parse(s)).orElse(null);
	}

	@Override
	public String marshal(LocalDate v) throws Exception {
		return Optional.ofNullable(v).map(LocalDate::toString).orElse(null);
	}

}
