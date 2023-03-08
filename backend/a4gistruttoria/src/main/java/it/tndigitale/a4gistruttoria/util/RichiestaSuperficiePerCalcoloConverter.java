package it.tndigitale.a4gistruttoria.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.InformazioniColtivazione;
import it.tndigitale.a4gistruttoria.dto.Particella;
import it.tndigitale.a4gistruttoria.dto.lavorazione.RichiestaSuperficiePerCalcoloDto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.RiferimentiCartografici;
import it.tndigitale.a4gistruttoria.repository.model.A4gtRichiestaSuperficie;

@Component
public class RichiestaSuperficiePerCalcoloConverter
		implements Converter<A4gtRichiestaSuperficie, RichiestaSuperficiePerCalcoloDto> {
	
	private static final Logger logger = LoggerFactory.getLogger(RichiestaSuperficiePerCalcoloConverter.class);

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public RichiestaSuperficiePerCalcoloDto convert(A4gtRichiestaSuperficie from) {
		RichiestaSuperficiePerCalcoloDto to = new RichiestaSuperficiePerCalcoloDto();
		try {
			BeanUtils.copyProperties(from, to);
			to.setCodiceInterventoAgs(from.getIntervento().getIdentificativoIntervento());
			to.setIdInterventoDu(from.getIntervento().getId());
			to.setIdDomanda(from.getDomandaUnicaModel().getId());
			to.setAnnoCampagna(from.getDomandaUnicaModel().getCampagna().longValue());
			to.setInfoCatastali(objectMapper.readValue(from.getInfoCatastali(), Particella.class));
			to.setInfoColtivazione(objectMapper.readValue(from.getInfoColtivazione(), InformazioniColtivazione.class));
			to.setRiferimentiCartografici(objectMapper.readValue(from.getRiferimentiCartografici(), RiferimentiCartografici.class));
		} catch (Exception e) {
			logger.error("Errore in conversione RichiestaSuperficiePerCalcoloDto", e);
		}
		return to;
	}

}
