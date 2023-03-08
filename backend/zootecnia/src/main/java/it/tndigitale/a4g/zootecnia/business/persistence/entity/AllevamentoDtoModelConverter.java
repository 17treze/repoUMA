package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.zootecnia.dto.AnagraficaAllevamentoDto;
import it.tndigitale.a4g.zootecnia.dto.FascicoloDto;


@Component
public class AllevamentoDtoModelConverter implements Converter<AllevamentoModel, AnagraficaAllevamentoDto> {

	@Autowired
	FascicoloModelDtoConverter fascicoloConverter;
	
	@Override
	public AnagraficaAllevamentoDto convert(AllevamentoModel source) {
		if (source == null) {
			return null;
		}
		AnagraficaAllevamentoDto dest = new AnagraficaAllevamentoDto();
		dest.setIdentificativo(source.getIdentificativo());
		dest.setAutorizzazioneSanitariaLatte(source.getAutorizzazioneSanitariaLatte());
		dest.setCfDetentore(source.getCfDetentore());
		dest.setCfProprietario(source.getCfProprietario());
		dest.setIdentificativoFiscale(source.getIdentificativoFiscale());
		dest.setDenominazioneDetentore(source.getDenominazioneDetentore());
		dest.setDenominazioneProprietario(source.getDenominazioneProprietario());
		dest.setDtChiusuraAllevamento(source.getDtChiusuraAllevamento());
		dest.setDtFineDetenzione(source.getDtFineDetenzione());
		dest.setDtAperturaAllevamento(source.getDtAperturaAllevamento());
		dest.setDtInizioDetenzione(source.getDtInizioDetenzione());
		dest.setOrientamentoProduttivo(source.getOrientamentoProduttivo());
		dest.setSoccida(source.getSoccida());
		dest.setSpecie(source.getSpecie());
//		gestione struttura allevamento
		dest.setStrutturaAllevamentoDto(StrutturaAllevamentoMapper.from(source)); 
		dest.setTipologiaAllevamento(source.getTipologiaAllevamento());
		dest.setTipoProduzione(source.getTipologiaProduzione());
		FascicoloDto fascicoloDto = fascicoloConverter.convert(source.getFascicolo());
		dest.setFascicolo(fascicoloDto);
		return dest;
	}
}
