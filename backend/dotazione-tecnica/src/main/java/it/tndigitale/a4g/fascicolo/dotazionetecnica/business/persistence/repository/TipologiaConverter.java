package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.TipologiaAbstractModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.TipologiaDto;
import it.tndigitale.a4g.framework.time.Clock;

@Component
public class TipologiaConverter implements Converter<TipologiaAbstractModel, TipologiaDto> {

	@Autowired
	private Clock clock;

	@Override
	public TipologiaDto convert(TipologiaAbstractModel source) {
		LocalDateTime now = clock.now();
		//now  deve essere dopo dt inizio e -- data fine è null || now è prima di data fine --
		if (now.isAfter(source.getDataInizio()) && (source.getDataFine() == null  || now.isBefore(source.getDataFine()))) {
			return new TipologiaDto()
					.setId(source.getId())
					.setDescrizione(source.getDescrizione());
		}
		return null;
	}

}
