package it.tndigitale.a4g.zootecnia.business.persistence.entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto;

@Component
public class AllevamentoModelDtoConverter implements Converter<AnagraficaAllevamentoDto, AllevamentoModel> {

	@Override
	public AllevamentoModel convert(AnagraficaAllevamentoDto source) {
		if (source == null) {
			return null;
		}
		AllevamentoModel dest = new AllevamentoModel();
		dest.setAutorizzazioneSanitariaLatte(source.getAutorizzazioneLatte());
		dest.setCfDetentore(source.getCfDetentore());
		dest.setCfProprietario(source.getCfProprietario());
		dest.setDenominazioneDetentore(source.getDenominazioneDetentore());
		dest.setDenominazioneProprietario(source.getDenominazioneProprietario());
		dest.setDtAperturaAllevamento(source.getDtInizioAttivita());
		dest.setDtChiusuraAllevamento(source.getDtFineAttivita());
		dest.setDtFineDetenzione(source.getDtFineDetentore());
		dest.setDtInizioDetenzione(source.getDtInizioDetentore());
		dest.setIdentificativo(source.getIdentificativo().toString());
		dest.setIdentificativoFiscale(source.getCfProprietario());
		dest.setOrientamentoProduttivo(source.getOrientamentoProduttivo());
		dest.setSoccida(source.getSoccida());
		dest.setSpecie(source.getSpecie());
		dest.setTipologiaAllevamento(source.getTipologiaAllevamentoCodice());
		dest.setTipologiaProduzione(source.getTipoProduzione());
		dest.setIdValidazione(0);
		return dest;
	}
}
