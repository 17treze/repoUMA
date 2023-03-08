package it.tndigitale.a4g.fascicolo.anagrafica.dto.persona;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IscrizioneSezioneModel;

@Component
public class IscrizioneSezioneConverter implements Converter<IscrizioneSezioneModel, IscrizioneSezioneDto> {

	@Override
	public IscrizioneSezioneDto convert(final IscrizioneSezioneModel source) {
		IscrizioneSezioneDto dest = new IscrizioneSezioneDto();
		dest.setColtivatoreDiretto(source.getColtivatoreDiretto());
		dest.setDataIscrizione(source.getDataIscrizione());
		dest.setQualifica(source.getQualifica());
		dest.setSezione(source.getSezione());
		return dest;
	}
}

