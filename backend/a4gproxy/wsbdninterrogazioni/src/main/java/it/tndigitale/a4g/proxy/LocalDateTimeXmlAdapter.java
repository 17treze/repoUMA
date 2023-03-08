/**
 * 
 */
package it.tndigitale.a4g.proxy;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Classe per la conversione da usare in fase di generazione
 * da xml a java
 * 
 * @author it417
 *
 */
public class LocalDateTimeXmlAdapter extends XmlAdapter<String, LocalDateTime> {

	@Override
	public LocalDateTime unmarshal(String v) throws Exception {
		return Optional.ofNullable(v).filter(s -> !s.isEmpty()).map(s -> LocalDateTime.parse(s)).orElse(null);
	}

	@Override
	public String marshal(LocalDateTime v) throws Exception {
		return Optional.ofNullable(v).map(LocalDateTime::toString).orElse(null);
	}

}
